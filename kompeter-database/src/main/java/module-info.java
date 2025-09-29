module kompeter.database {
    requires transitive java.sql;
    requires kompeter.utilities;
    requires org.xerial.sqlitejdbc;
    requires io.github.classgraph;
    requires static org.jetbrains.annotations;

    exports com.github.ragudos.kompeter.database;
    exports com.github.ragudos.kompeter.database.migrations;
    exports com.github.ragudos.kompeter.database.seeder;
    exports com.github.ragudos.kompeter.database.sqlite;
    exports com.github.ragudos.kompeter.database.dao.impl.sqlite;
    exports com.github.ragudos.kompeter.database.dao;
    exports com.github.ragudos.kompeter.database.dto.InventoryDTOs;
    exports com.github.ragudos.kompeter.database.dto.SalesDTOs;
    exports com.github.ragudos.kompeter.database.dto.StockDTOs;
    
}
