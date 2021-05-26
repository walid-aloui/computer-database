
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.ui.Cli;

public class MainApp {

	public static void main(String[] args) throws OpenException, MapperException, ExecuteQueryException {
		Cli cli = new Cli();
		cli.runCli();
	}

}