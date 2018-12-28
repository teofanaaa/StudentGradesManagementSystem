package service;

import domain.Student;
import domain.Tema;
import repository.Repository;
import utils.DataChanged;
import utils.Observable;
import validator.ValidationException;

import java.util.Comparator;
import java.util.List;

import static utils.Config.filterAndSorter;


public class TemaService extends AbstractService<String, Tema> {
    Repository<String, Student> repoS;

    public TemaService(Repository<String, Tema> repository,Repository<String, Student> repoS) {
        super(repository);
        this.repoS=repoS;
    }


    public Tema modificareDeadline(String id, String deadlineVechiString, String deadlineNouString) throws ValidationException{
        Tema tema=find(id);
        if(tema!=null){
            int deadlineVechi=Integer.parseInt(deadlineVechiString);
            if(deadlineVechi<utils.Config.getCurrentWeek()){
                throw new ValidationException("S-a depasit deadline-ul vechi!");
            }

            int dataPredare=Integer.parseInt(tema.getDataPredare());
            int deadline=Integer.parseInt(tema.getDeadline());
            if(dataPredare>deadline) {
                throw new ValidationException("Deadline-ul e mai mic fata de data de predare a temei!");
            }
            tema.setDeadline(deadlineNouString);

        }
        return update(tema);
    }

    @Override
    public Tema update(Tema entity) throws ValidationException {
        Tema tema=find(entity.getID());
        if(tema!=null){
            if(entity.getDeadline().equals("")) entity.setDeadline(tema.getDeadline());
            if(entity.getDescriere().equals("")) entity.setDescriere(tema.getDescriere());
            if(entity.getDataPredare().equals("")) entity.setDataPredare(tema.getDataPredare());
        }
        return super.update(entity);
    }

    public List<Tema> filtreazaTemaDeadline(String deadline) {
        return filterAndSorter(getAll(), entity -> entity.getDeadline().equals(deadline), Comparator.comparing(Tema::getDeadline));
    }

    public List<Tema> sorteazaTemaDeadline() {
        return filterAndSorter(getAll(), null, Comparator.comparing(Tema::getDeadline));
    }

    public List<Tema> filtreazaTemaKeyword(String keyword) {
        return filterAndSorter(getAll(),
                entity -> entity.getDescriere().toLowerCase().contains(keyword.toLowerCase()) ||
                        String.valueOf(entity.getDeadline()).contains(keyword),
                Comparator.comparing(Tema::getID));
    }



}
