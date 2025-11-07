package kompeter.database.dto.etc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Supplier {
    final int id;
    final String name;
}
