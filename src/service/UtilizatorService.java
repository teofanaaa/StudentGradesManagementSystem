package service;

import domain.Profesor;
import domain.Student;
import domain.Utilizator;
import repository.Repository;
import utils.Config;
import utils.DataChanged;
import utils.Observer;
import utils.PasswordStorage;
import validator.ValidationException;

import java.util.Comparator;
import java.util.List;

import static utils.Config.filterAndSorter;
import static utils.PasswordStorage.createHash;
import static utils.PasswordStorage.verifyPassword;

/**
 * Clasa UtilizatorService
 * Contul de administrator: admin, parola: admin
 * Contul de student: username de la email (pana la @...), parola: numele de familie dat (student.getNume())
 * Contul de profesor: username de la email (pana la @...), parola: numele complet (profesor.getNume())
 */
public class UtilizatorService extends AbstractService<String, Utilizator>{

    /**
     * Constructorul clasei
     * @param repository - repository de Utilizator
     */
    public UtilizatorService(Repository<String, Utilizator> repository) {
        super(repository);
    }

    /**
     * Actualizare date Utilizator
     * @param entity (utilizator de actualizat)
     * @return entity (nu s-a putut face actualizarea)/null(s-a facut actualizarea)
     * @throws ValidationException (date invalide)
     */
    @Override
    public Utilizator update(Utilizator entity) throws ValidationException {
        Utilizator returned=find(entity.getID());
        if(returned!=null){
            if(entity.getNume().equals("")) entity.setNume(returned.getNume());
            if(entity.getHash().equals("")) entity.setHash(returned.getHash());
            //if(entity.getTip().equals("")) entity.setTip(returned.getTip());
        }
        return super.update(entity);
    }

    /**
     * Crearea unui utilizator de tip STUDENT
     * @param student - Student (studentul caruia i se face contul)
     * @return Utilizator
     * @throws PasswordStorage.CannotPerformOperationException
     */
    public Utilizator createUser(Student student) throws PasswordStorage.CannotPerformOperationException {
        String username=student.getEmail().split("@")[0];
        String nume=student.getNume()+" "+student.getPrenume();
        String parola=student.getNume();
        String hash= createHash(parola);
        return add(new Utilizator(username, hash, Utilizator.TipUtilizator.STUDENT,nume));
    }

    /**
     * Crearea unui utilizator de tip PROFESOR
     * @param profesor - Profesor (profesorul caruia i se face contul)
     * @return Utilizator
     * @throws PasswordStorage.CannotPerformOperationException
     */
    public Utilizator createUser(Profesor profesor) throws PasswordStorage.CannotPerformOperationException {
        String username=profesor.getEmail().split("@")[0];
        String nume=profesor.getNume();
        String parola=profesor.getNume();
        String hash= createHash(parola);
        return add(new Utilizator(username, hash, Utilizator.TipUtilizator.PROFESOR,nume));

    }

    /**
     * Crearea unui utilizator de tip ADMIN
     * @return Utilizator
     * @throws PasswordStorage.CannotPerformOperationException
     */
    public Utilizator createUser() throws PasswordStorage.CannotPerformOperationException {
        String username="admin";
        String nume="Administrator";
        String parola="admin";
        String hash= createHash(parola);
        return add(new Utilizator(username, hash, Utilizator.TipUtilizator.ADMIN,nume));
    }

    /**
     * Verificare parola
     * @param username - string
     * @param password - string
     * @return true(date corecte)/ false (date invalide)
     */
    public boolean verificareParola(String username, String password) {
        Utilizator user = find(username);

        if (user!=null) {
            String userHash = user.getHash();
            try {
                return PasswordStorage.verifyPassword(password, userHash);
            } catch (PasswordStorage.CannotPerformOperationException e) {
                e.printStackTrace();
            } catch (PasswordStorage.InvalidHashException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Modificare parola
     * @param user - string (nume de utilizator)
     * @param oldPassword - string (parola veche)
     * @param newPassword - string (noua parola)
     * @param newPasswordRetype - string (noua parola)
     */
//    public void modificareParola(String user, String oldPassword, String newPassword, String newPasswordRetype) {
//        Utilizator userFound = find(user);
//        if (userFound==null)
//            throw (new ValidationException("Userul " + user + " nu exista."));
//
//        String msg = "";
//        if (newPassword.length() < 5)
//            msg += "Parola trebuie sa contina cel putin 5 caractere. ";
//        if (! newPassword.equals(newPasswordRetype))
//            msg += "Parolele nu coincid. ";
//                try {
//                    if (! verifyPassword(user, oldPassword)) {
//                        msg += "Parola veche nu este corecta. ";
//                    }
//                } catch (PasswordStorage.CannotPerformOperationException e) {
//                    e.printStackTrace();
//                } catch (PasswordStorage.InvalidHashException e) {
//                    e.printStackTrace();
//                }
//                if (! msg.equals(""))
//            throw new ValidationException(msg);
//
//        String hash = null;
//        try {
//            hash = createHash(newPassword);
//        } catch (PasswordStorage.CannotPerformOperationException e) {
//            throw new ValidationException("Nu s-a putut realiza modificarea.");
//        }
//
//        update(new Utilizator(user, hash, userFound.getTip(),userFound.getNume()));
//    }

    /**
     * Resetare parola
     * @param user - string (nume de utilizator)
     * @throws ValidationException
     * @throws PasswordStorage.CannotPerformOperationException
     */
//    public void resetareParola(Utilizator user)
//            throws ValidationException, PasswordStorage.CannotPerformOperationException {
//        String newHash = PasswordStorage.createHash(user.getID());
//        user.setHash(newHash);
//        update(user);
//    }

    /**
     * Filtrare utilizator dupa keyword
     * @param keyword
     * @return lista de utilizatori
     */
    public List<Utilizator> filtreazaUseriKeyword(String keyword) {
        return filterAndSorter(getAll(),
                entity->entity.getNume().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getID().toLowerCase().contains(keyword.toLowerCase()),
                Comparator.comparing(Utilizator::getID)
        );
    }
}
