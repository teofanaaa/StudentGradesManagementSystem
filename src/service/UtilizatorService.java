package service;

import domain.Profesor;
import domain.Student;
import domain.Utilizator;
import repository.Repository;
import utils.DataChanged;
import utils.Observer;
import utils.PasswordStorage;
import validator.ValidationException;

import static utils.PasswordStorage.createHash;
import static utils.PasswordStorage.verifyPassword;

public class UtilizatorService extends AbstractService<String, Utilizator>{

    public UtilizatorService(Repository<String, Utilizator> repository) {
        super(repository);
    }

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

    public Utilizator createUser(Student student) throws PasswordStorage.CannotPerformOperationException {
        String username=student.getEmail().split("@")[0];
        String nume=student.getNume()+" "+student.getPrenume();
        String parola=student.getNume();
        String hash= createHash(parola);
        return add(new Utilizator(username, hash, Utilizator.TipUtilizator.STUDENT,nume));

    }

    public Utilizator createUser(Profesor profesor) throws PasswordStorage.CannotPerformOperationException {
        String username=profesor.getEmail().split("@")[0];
        String nume=profesor.getNume();
        String parola=profesor.getNume();
        String hash= createHash(parola);
        return add(new Utilizator(username, hash, Utilizator.TipUtilizator.PROFESOR,nume));

    }

    public Utilizator createUser() throws PasswordStorage.CannotPerformOperationException {
        String username="admin";
        String nume="Administrator";
        String parola="admin";
        String hash= createHash(parola);
        return add(new Utilizator(username, hash, Utilizator.TipUtilizator.ADMIN,nume));
    }

    public boolean verificareParola(String username, String password)
            throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        Utilizator user = find(username);

        if (user!=null) {
            String userHash = user.getHash();
            return verifyPassword(password, userHash);
        }
        else
            return false;
    }

    public void modificareParola(String user, String oldPassword, String newPassword, String newPasswordRetype)
            throws ValidationException, PasswordStorage.InvalidHashException,
            PasswordStorage.CannotPerformOperationException {

        Utilizator userFound = find(user);
        if (userFound==null)
            throw (new ValidationException("Userul " + user + " nu exista."));

        String msg = "";
        if (newPassword.length() < 5)
            msg += "Parola trebuie sa contina cel putin 5 caractere. ";
        if (! newPassword.equals(newPasswordRetype))
            msg += "Parolele nu coincid. ";
        if (! verifyPassword(user, oldPassword)) {
            msg += "Parola veche nu este corecta. ";
        }
        if (! msg.equals(""))
            throw new ValidationException(msg);

        String hash = null;
        try {
            hash = createHash(newPassword);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            throw new ValidationException("Nu s-a putut realiza modificarea.");
        }

        update(new Utilizator(user, hash, userFound.getTip(),userFound.getNume()));
    }

    public void resetareParola(Utilizator user)
            throws ValidationException, PasswordStorage.CannotPerformOperationException {
        String newHash = PasswordStorage.createHash(user.getID());
        user.setHash(newHash);
        update(user);

    }
}
