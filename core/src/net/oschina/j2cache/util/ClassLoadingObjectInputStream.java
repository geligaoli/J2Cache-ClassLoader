package net.oschina.j2cache.util;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;


/**
 * ClassLoadingObjectInputStream
 *
 * For re-inflating serialized objects, this class uses the thread context classloader
 * rather than the jvm's default classloader selection.
 * 
 */
public class ClassLoadingObjectInputStream extends ObjectInputStream
{
    private ClassLoader _classLoader;
    
    /* ------------------------------------------------------------ */
    public ClassLoadingObjectInputStream(java.io.InputStream in, ClassLoader classLoader) throws IOException
    {
        super(in);
        this._classLoader = classLoader;
    }

    /* ------------------------------------------------------------ */
    public ClassLoadingObjectInputStream(java.io.InputStream in) throws IOException
    {
        super(in);
        this._classLoader = Thread.currentThread().getContextClassLoader();
    }

    /* ------------------------------------------------------------ */
    public ClassLoadingObjectInputStream () throws IOException
    {
        super();
    }

    /* ------------------------------------------------------------ */
    @Override
    public Class<?> resolveClass (java.io.ObjectStreamClass cl) throws IOException, ClassNotFoundException
    {
        try
        {
            return Class.forName(cl.getName(), false, this._classLoader);
        }
        catch (ClassNotFoundException e)
        {
            return super.resolveClass(cl);
        }
    }
    
    /* ------------------------------------------------------------ */
    @Override
    protected Class<?> resolveProxyClass(String[] interfaces)
            throws IOException, ClassNotFoundException
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ClassLoader nonPublicLoader = null;
        boolean hasNonPublicInterface = false;

        // define proxy in class loader of non-public interface(s), if any
        Class<?>[] classObjs = new Class<?>[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            Class<?> cl = Class.forName(interfaces[i], false, loader);
            if ((cl.getModifiers() & Modifier.PUBLIC) == 0) {
                if (hasNonPublicInterface) {
                    if (nonPublicLoader != cl.getClassLoader()) {
                        throw new IllegalAccessError(
                            "conflicting non-public interface class loaders");
                    }
                } else {
                    nonPublicLoader = cl.getClassLoader();
                    hasNonPublicInterface = true;
                }
            }
            classObjs[i] = cl;
        }
        try {
            return Proxy.getProxyClass( hasNonPublicInterface ? nonPublicLoader : loader, classObjs);
        } catch (IllegalArgumentException e) {
            throw new ClassNotFoundException(null, e);
        }
    }
}

