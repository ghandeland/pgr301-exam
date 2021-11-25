import com.pgr301.exam.BankAccountApplication;
import com.pgr301.exam.model.Account;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankAccountApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ModelTest {

    @Test
    public void testAccount() {
        Account account = new Account();

        BigDecimal bd = new BigDecimal("0");
        assertThat(account.getBalance(), Matchers.comparesEqualTo(bd));
        assertEquals(account.getCurrency(), "NOK");
    }
}
