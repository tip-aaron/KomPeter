/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.menu;

import kompeter.database.dto.users.Session;
import kompeter.services.auth.SessionManager;
import raven.modal.drawer.menu.MenuValidation;

public class KompeterMenuValidation extends MenuValidation {
    private static final int TAB_INVENTORY = 2;
    private static final int TAB_LOGOUT = 4;
    private static final int TAB_MONITORING = 3;
    private static final int TAB_POS = 1;
    private static final int TAB_PROFILE = 0;

    @Override
    public boolean menuValidation(final int[] index) {
        final Session session = SessionManager.getInstance().getSession();

        if (session == null) {
            return false;
        }

        if (session.getUser().isAdmin()) {
            return true;
        }

        final boolean allowedTabs = index[0] == TAB_PROFILE || index[0] == TAB_LOGOUT;

        if (session.getUser().isCashier()) {
            return allowedTabs || index[0] == TAB_POS || index[0] == TAB_MONITORING;
        }

        if (session.getUser().isInventoryClerk()) {
            return allowedTabs || index[0] == TAB_INVENTORY || index[0] == TAB_MONITORING;
        }

        return allowedTabs;
    }
}
