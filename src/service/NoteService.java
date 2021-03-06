package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import repository.Repository;
import utils.*;
import validator.ValidationException;

import java.io.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static utils.Config.filterAndSorter;
import static utils.Config.getWeek;
import static utils.Config.getWeekUni;

public class NoteService extends AbstractService<Pair<String,String>, Nota> {
    public Repository<String, Student> getRepoS() {
        return repoS;
    }

    Repository<String, Student> repoS;
    Repository<String, Tema> repoT;

    /**
     * Getter pentru repo
     * @return
     */
    public Repository<String, Tema> getRepoT() {
        return repoT;
    }

    private enum Action {
        ADD, UPDATE
    }

    /**
     * Constructorul clasei
     * @param repository - repository de note
     * @param repoS - repository de studenti
     * @param repoT - repository de teme
     */
    public NoteService(Repository<Pair<String, String>, Nota> repository, Repository<String, Student> repoS,
            Repository<String, Tema> repoT) {
        super(repository);
        this.repoS=repoS;
        this.repoT=repoT;
    }

    /**
     * Aplicarea penalizari unei note
     * @param entity -Nota (nota pentru care se aplica penalizari)
     * @return string (mesajul corespunzator penalizarii)
     */
    private String aplicarePenalizari(Nota entity) {
        float nota=Float.parseFloat(entity.getNotaProf());
        Tema tema=repoT.findOne(Optional.of(entity.getTemaID())).get();
        int dataCurenta=Integer.parseInt(entity.getDataCurenta());
        int deadline=Integer.parseInt(tema.getDeadline());
        int dif = dataCurenta -deadline;

        if (dif > 0 && dif <= 2) {
            Float nouaNota=nota - dif * 2.5f;
            entity.setNotaProf(nouaNota.toString());
            return "Nota a fost diminuata cu "+dif*2.5f+" pucte din cauza intarzierii. ";
        } else if (dif<=0){
            return "Tema a fost predata la timp. ";
        }
        else{
            entity.setNotaProf("1");
            return "S-a acordat nota din oficiu! Tema nu a fost predata la timp. ";
        }
    }

    /**
     * Creaza mesajul de scris in fisier la adaugarea unei note
     * @param nota - Nota
     * @param feedback - string (mesaj de la profesor)
     * @param action - enum (ADD, UPDATE)
     * @return string (textul de scris in fisier)
     */
    private String writeContent(Nota nota,String feedback, Action action){
        String msg;
        if(action==Action.ADD)
            msg="> S-a adaugat o nota noua!\n";
        else msg="> S-a modificat o nota existenta!\n";
        msg=msg+"Tema: "+nota.getTemaID()+"\n";
        msg=msg+"Nota: "+nota.getNotaProf()+"\n";
        Tema tema=repoT.findOne(Optional.of(nota.getTemaID())).get();
        msg=msg+"Predata in saptamana: "+tema.getDataPredare()+"\n";
        msg=msg+"Deadline: "+tema.getDeadline()+"\n";
        msg=msg+"Feedback: "+feedback+"\n";
        return msg;
    }

    /**
     * Adaugare text in fisier
     * @param nota - Nota (nota de adaugat)
     * @param feedback - string (mesaj de la profesor)
     * @param action - enum (ADD, UPDATE)
     */
    private void adaugaInFile(Nota nota, String feedback, Action action){
        Student student=repoS.findOne(Optional.of(nota.getStudentID())).get();

        String fileName= Config.pathRezultate +student.getNume()+student.getPrenume()+".txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,true))) {
            File file=new File(fileName);
            if(file.length()==0) {
                bw.write("Situatia studentului "+student.getNume()+ " "+student.getPrenume()+" la disciplina MAP\n");
                bw.newLine();
            }
            bw.write(writeContent(nota,feedback,action));
            bw.newLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adaugare nota
     * @param idStudent - string
     * @param idTema - string
     * @param nota - string
     * @param feedback -string
     * @param motivat - boolean (studentul a lipsit sau nu motivat)
     * @return Nota
     */
    public Nota add(String idStudent, String idTema, String nota, String feedback, boolean motivat) {

        try {
            Integer dataCurenta = utils.Config.getWeekUni();
            Nota entity = new Nota(idStudent, idTema, dataCurenta.toString(), nota);
            String feedbackAutomat;
            if (!motivat) {
                feedbackAutomat = aplicarePenalizari(entity);
            } else feedbackAutomat = "Nu s-au aplicat penalizari. ";
            Nota returned = add(entity);
            if (returned == null) {
                notifyObservers(new DataChanged(EventType.ADD));
                adaugaInFile(entity, feedbackAutomat + feedback, Action.ADD);
            Student student=repoS.findOne(Optional.of(idStudent)).get();
            SendEmail email=new SendEmail("teofanaenachioiu@yahoo.com",
                    "Draga "+ student.getPrenume()+",\n\n"
                    +"Tocmai ai primit nota " +entity.getNotaProf()
                            +" la tema "+idTema+"\nFeedback la tema: "+feedbackAutomat+feedback+"\n\n"+
                            "Pe curand!",
                    "Nota noua"
                    );
            email.run();
            } else {
                GUIUtils.showErrorMessage("Studentul are deja nota!");
            }
            return returned;

        } catch (ValidationException e) {
            GUIUtils.showErrorMessage(e.getMessage());
        }
        return  null;
    }

    /**
     * Actualizare nota
     * @param idStudent - string
     * @param idTema - string
     * @param nota - string
     * @param feedback -string
     * @param motivat - boolean (studentul a lipsit sau nu motivat)
     * @return Nota
     */
    public Nota update(String idStudent, String idTema, String nota, String feedback, boolean motivat){
        Integer dataCurenta=utils.Config.getWeekUni();
        Nota entity=new Nota(idStudent,idTema,dataCurenta.toString(),nota);
        if(find(new Pair<>(idStudent,idTema))!=null) {
            String feedbackAutomat;
            if (!motivat) {
                feedbackAutomat = aplicarePenalizari(entity);
            } else feedbackAutomat="Nu s-au aplicat penalizari. ";
            notifyObservers(new DataChanged(EventType.UPDATE));
            adaugaInFile(entity,  feedbackAutomat+ feedback, Action.UPDATE);
        }
        return update(entity);
    }

    /**
     * Adauga entitatea in repo
     * @param entity (entitatea de adaugat)
     * @return Nota
     * @throws ValidationException (date invalide)
     */
    @Override
    public Nota add(Nota entity) throws ValidationException {
        if(!repoT.findOne(Optional.of(entity.getTemaID())).isPresent())
            return entity;
        if(!repoS.findOne(Optional.of(entity.getStudentID())).isPresent())
            return entity;
        if(super.find(new Pair(entity.getStudentID(),entity.getTemaID()))!=null)
            return entity;
        return super.add(entity);
    }

    /**
     * Actualizeaza nota
     * @param entity (nota de actualizat)
     * @return entity (daca nota nu a putut fi actualizata)/null (nota a fost actualizata)
     * @throws ValidationException (date invalide)
     */
    @Override
    public Nota update(Nota entity) throws ValidationException {
        Nota nota=find(entity.getID());
        if(nota!=null){//gasesac nota si actualizez datele
            if(entity.getDataCurenta().equals("")) entity.setDataCurenta(nota.getDataCurenta());
            if(entity.getNotaProf().equals("")) entity.setNotaProf(nota.getNotaProf());

        }
        return super.update(entity);
    }

    /**
     * Aplicare penalizari la catualizare
     * @param entity - Nota (entitatea de adaugat)
     * @param motivat - boolean (studentul a lipsit notivat sau nu)
     * @return entity (daca nota nu a putut fi actualizata)/null (nota a fost actualizata)
     * @throws ValidationException
     */
    public Nota update(Nota entity, boolean motivat) throws ValidationException {
        Nota returned=this.update(entity);
        if(returned==null && !motivat){//gasesac nota si actualizez datele
            entity.setNotaProf(aplicarePenalizari(entity).toString());
        }
        return returned;
    }

    /**
     * Lista notelor unui student
     * @param idStudent - string (id-ul studentului)
     * @return lista de Note
     */
    public ArrayList<Nota> listaNoteStudent(String idStudent){
        return (ArrayList<Nota>) super.getAll().stream()
                .filter(x->x.getStudentID().equals(idStudent))
                .collect(Collectors.toList());
    }

    /**
     * Lista notelor la o tema
     * @param idTema - string (id tema)
     * @return lista de Note
     */
    public List<Nota> listaNoteTema(String idTema){
        return super.getAll().stream()
                .filter(x->x.getTemaID().equals(idTema))
                .collect(Collectors.toList());
    }

    /**
     * Lista de note a unei grupe
     * @param grupa - string
     * @return lista de note
     */
    public List<Nota> listaNoteGrupa(String grupa){
        return super.getAll().stream()
                .filter(x->repoS.findOne(Optional.of(x.getStudentID())).get().getGrupa().equals(grupa))
                .collect(Collectors.toList());
    }

//    public void stergeNoteStudent(String idStudent){
//        for(Nota nota:listaNoteStudent(idStudent)){
//            super.remove(nota.getID());
//        }
//    }
//
//    public void stergeNoteTema(String idTema){
//        for(Nota nota:listaNoteTema(idTema)){
//            super.remove(nota.getID());
//        }
//    }
//
//    public List<Nota> filtreazaNoteSub(String nota) {
//        return filterAndSorter(getAll(), entity -> Double.parseDouble(entity.getNotaProf()) <= Double.parseDouble(nota), null);
//    }
//
//    public List<Nota> filtreazaNoteIdTema(String id_tema) {
//        return filterAndSorter(getAll(), entity -> entity.getTemaID().equals( id_tema), null);
//    }
//
//    public List<Nota> filtreazaNoteIdStudent(String id_student) {
//        return filterAndSorter(getAll(), entity -> entity.getStudentID().equals( id_student), null);
//    }
//
//    public List<Nota> filtreazaNoteSaptamana(String saptamana) {
//        return filterAndSorter(getAll(), entity -> entity.getDataCurenta().equals(saptamana), null);
//    }
//
//    public List<Nota> filtreazaGrupaTema(String grupa,String idTema) {
//        return filterAndSorter(listaNoteGrupa(grupa), entity -> entity.getTemaID().equals(idTema), null);
//    }
//
//    public List<Nota> sorteazaNote() {
//        return filterAndSorter(getAll(), null,
//                Comparator.comparing(Nota::getTemaID)
//                        .thenComparing(Nota::getStudentID));
//    }
//
//    public List<Nota> filtreazaNoteInterval(LocalDate start,LocalDate end) {
//        return filterAndSorter(getAll(),
//                entity -> Integer.parseInt(entity.getDataCurenta())>=getWeekUni(getWeek(start))
//                &&Integer.parseInt(entity.getDataCurenta())<=getWeekUni(getWeek(end)),
//                null);
//    }
//
//    public List<Nota> filtreazaGrupa(String grupa) {
//        return filterAndSorter(getAll(), entity -> repoS.findOne(Optional.of(entity.getStudentID()))
//                .get().getGrupa().equals(grupa), null);
//    }

    /**
     * Media notelor unui student
     * @param idStudent - string (id-ul studentului)
     * @return double (media studentului)
     */
    public Double getMediaStudent(String idStudent) {
        double suma = 0;
        int pondere=1;

        Tema tema;
        if(listaNoteStudent(idStudent)==null)
            return 0d;
        for ( Nota nota : listaNoteStudent(idStudent)) {
            tema=repoT.findOne(Optional.of(nota.getTemaID())).get();
            pondere=Integer.parseInt(tema.getDeadline())-Integer.parseInt(tema.getDataPredare());
            suma =suma+ Double.parseDouble(nota.getNotaProf())*pondere;

        }

        int pods=0;
        for(Tema t: repoT.findAll()){
            pondere=Integer.parseInt(t.getDeadline())-Integer.parseInt(t.getDataPredare());
            pods+=pondere;
        }

        return suma /pods ;
    }

    /**
     * Media notelor pe fiecare tema (=laborator)
     * @param tema - string (id tema)
     * @param allStudents - lista de studenti
     * @return double (media)
     */
    public Double getMediaLaborator(Tema tema, List<Student> allStudents){
        double suma = 0;
        if(allStudents.size()==0)
            return 0d;
        for ( Student student:allStudents) {
            Nota nota=find(new Pair<>(student.getID(),tema.getID()));
            if(nota!=null){
                suma =suma+ Double.parseDouble(nota.getNotaProf());

            }
        }
        return suma / allStudents.size();
    }

    /**
     * Numarul de studenti din fiecare grupa
     * @param grupa
     * @return integer
     */
    private Integer nrStudentiGrupa(String grupa){
        return getAll()
                .stream()
                .filter(nota -> repoS.findOne(Optional.of(nota.getStudentID())).get().getGrupa().equals(grupa))
                .collect(Collectors.toList())
                .size();
    }

    /**
     * Media studentilor pe fiecare grupa
     * @param grupa - string (grupa)
     * @return double (media)
     */
    public double getMedieGrupa(String grupa){
        double suma = 0;
        int pondere=1;
        Tema tema;
        for ( Nota nota : listaNoteGrupa(grupa)) {
            tema=repoT.findOne(Optional.of(nota.getTemaID())).get();
            pondere=Integer.parseInt(tema.getDeadline())-Integer.parseInt(tema.getDataPredare());
            suma =suma+ Double.parseDouble(nota.getNotaProf())*pondere;
        }
        return suma / (14*nrStudentiGrupa(grupa));
    }

    /**
     * Notarea studentilor cu 1 dupa terminarea saptamanii 14
     * Studentii care nu au note la un laborator, primesc nota 1 din oficiu
     */
    public void notareDinOficiu(){
        repoS.findAll().forEach(student->{
            repoT.findAll().forEach(tema->{
                Nota nota=find(new Pair<>(student.getID(),tema.getID()));
                int deadline=Integer.parseInt(tema.getDeadline());
                if(nota==null && getWeekUni()==null) {
                    Nota toAdd=new Nota(student.getID(), tema.getID(), getWeekUni().toString(), "1");
                    add(toAdd);
                    adaugaInFile(toAdd,"Tema nu a fost predata. ",Action.ADD);
                }
            });
        });
    }

    /**
     * Numarul studentilor cu o medie cuprinsa intre doua valori
     * @param strat - int
     * @param end - int
     * @return integer (numarul de studenti)
     */
    public Integer nrStudentiMedie(int strat, int end){
        Integer nr=0;
        for(Student student:repoS.findAll()){
            double media=getMediaStudent(student.getID());
            if(media>=strat &&media<end)
                nr++;
        }
        return nr;
    }

}
