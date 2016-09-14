package com.dong.frame.view;

import java.lang.reflect.Field;

import android.app.Activity;
import android.util.Log;
import android.view.View;

public class ViewAttacher {
    private static final String TAG = "ViewAttacher";

    public static void attach(Activity activity) {
        attach(activity.getWindow().getDecorView(), activity, false);
    }

    public static void attach(Activity activity, boolean b) {
        attach(activity.getWindow().getDecorView(), activity, b);
    }

    public static void attach(View v, Object o) {
        attach(v, o, false);
    }

    /**
     * 将o的field和v的子view绑定
     *
     * @param v View
     * @param o o
     * @param b 是否只绑定标记的field
     */
    public static void attach(View v, Object o, boolean b) {
        if (v == null || o == null)
            return;

        // 外部的R.id
        Class exIDClz = getIDClzFromAnchorR(o.getClass().getAnnotation(
                ViewAnchor.R.class));
        if (exIDClz == null) {
            exIDClz = getPackageIDClz(v);
        }

        Field[] fields = o.getClass().getDeclaredFields();

        for (Field f : fields) {
            if (View.class.isAssignableFrom(f.getType())
                    && f.getAnnotation(ViewAnchor.Skip.class) == null) {
                int id = getIDValueFromField(f, exIDClz, b);

                if (id != 0) {
                    attachViewToField(o, f, v.findViewById(id));
                }
            }
        }
    }

    /**
     * 获取Field对应的资源ID--------- -----------------------------
     * 1:如果是ViewAnchor标记，切带有id，则返回此id
     * 2:如果是ViewAnchor标记，不带id，则从外部的R文件中读取id
     * 3:如果是ViewAnchor.R标记，则从ViewAnchor.R指定的包里读取id
     * 4:如果field没有任何标记，且b为false，则从外部R中读取
     *
     * @param f
     * @return
     */
    private static int getIDValueFromField(Field f, Class exIDClz, boolean b) {
        ViewAnchor anchor = f.getAnnotation(ViewAnchor.class);
        ViewAnchor.R anchorR = f.getAnnotation(ViewAnchor.R.class);

        int idValue = 0;
        Class idClz = null;
        if (anchor != null) {
            idValue = anchor.value();
            if (idValue == 0) {
                idClz = exIDClz;
            }
        } else if (anchorR != null) {
            idClz = getIDClzFromAnchorR(anchorR);
        } else if (!b) {
            idClz = exIDClz;
        }

        if (idValue == 0 && idClz != null) {
            try {
                idValue = idClz.getDeclaredField(f.getName()).getInt(idClz);
            } catch (SecurityException e) {
                Log.i(TAG, "can't get id value from " + f.getName() + ", because of security exception");
            } catch (NoSuchFieldException e) {
                Log.i(TAG, "can't get id value from " + f.getName() + ", because no such file");
            } catch (IllegalArgumentException e) {
                Log.i(TAG, "can't get id value from " + f.getName() + ", because of IllegalArgumentException");
            } catch (IllegalAccessException e) {
                Log.i(TAG, "can't get id value from " + f.getName() + ", because of IllegalAccessException");
            }
        }

        return idValue;
    }

    private static Class getIDClzFromAnchorR(ViewAnchor.R anchorR) {
        if (anchorR == null) {
            return null;
        } else {
            Class rClz = anchorR.value();
            if (rClz != null) {
                try {
                    Class idClz = Class.forName(rClz.getName() + "$id");

                    return idClz;
                } catch (ClassNotFoundException e) {
                    Log.i(TAG, "can't get id class from " + anchorR.value());
                }
            }

            return null;
        }

    }

    /**
     * 获取默认的R.id
     *
     * @param v
     * @return
     */
    private static Class getPackageIDClz(View v) {
        String pkname = v.getContext().getPackageName();
        Class ridClz = null;
        try {
            ridClz = Class.forName(pkname + ".R$id");
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "can't find R:[" + pkname + ".R" + "]");
        }

        return ridClz;
    }

    /**
     * 给Field赋值 o.f = v;
     *
     * @param o 要赋值的对象
     * @param f 要赋值的field
     * @param v 赋值的view
     */
    private static void attachViewToField(Object o, Field f, View v) {
        f.setAccessible(true);
        try {
            f.set(o, f.getType().cast(v));
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "can't set view to " + f.getName());
        } catch (IllegalAccessException e) {
            Log.i(TAG, f.getName() + " can't access");
        }
    }

}
