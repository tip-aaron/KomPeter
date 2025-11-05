/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.seeder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface ISeeder {
    String getSeederQuery() throws FileNotFoundException, IOException;

    void seed() throws IOException, SQLException;
}
