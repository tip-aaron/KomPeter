package kompeter.database.seeder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface ISeeder {
    public String getSeederQuery() throws FileNotFoundException, IOException;

    public void seed() throws IOException, SQLException;
}
