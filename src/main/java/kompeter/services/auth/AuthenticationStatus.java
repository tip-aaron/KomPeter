/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.services.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationStatus {
    String message;
    StatusType statusType;

    public static enum StatusType {
        ERROR, SUCCESS;
    }
}
