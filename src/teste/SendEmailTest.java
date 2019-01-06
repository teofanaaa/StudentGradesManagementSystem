package teste;

import org.junit.jupiter.api.Test;
import utils.SendEmail;

import static org.junit.jupiter.api.Assertions.*;

class SendEmailTest {

    @Test
    void run() {
        SendEmail email=new SendEmail("teofanaenachioiu@yahoo.com",
                "Studentul Enachioiu Teofana a primit nota 10 la tema 5\nFeedback de la profesor: Foarte bine!",
                "Nota noua"
        );
        email.run();
    }
}