/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.system;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

// all logged in forms
public class AllForms {
    private static AllForms instance;

    private static AllForms getInstance() {
        if (instance == null) {
            instance = new AllForms();
        }
        return instance;
    }

    public static void clear() {
        getInstance().formsMap.forEach((key, val) -> {
            val.formClose();
            val.formDestroy();
        });
        getInstance().formsMap.clear();
    }

    public static void formInit(final Form form) {
        SwingUtilities.invokeLater(() -> form.formInit());
    }

    public static Form getForm(final Class<? extends Form> cls) {
        if (getInstance().formsMap.containsKey(cls)) {
            return getInstance().formsMap.get(cls);
        }

        try {
            final Form form = cls.getDeclaredConstructor().newInstance();
            getInstance().formsMap.put(cls, form);
            formInit(form);
            return form;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final Map<Class<? extends Form>, Form> formsMap;

    private AllForms() {
        formsMap = new HashMap<>();
    }
}
