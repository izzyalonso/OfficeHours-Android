package org.tndata.officehours.parser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.tndata.officehours.model.ResultSet;


/**
 * Main interface to the parser. It receives a string and a callback, generates a request
 * code, and fires up a task to do the parsing in the background, as the parsing may be
 * a heavy task in some instances where the user has a lot of content selected.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public final class Parser{
    private static final int MAX_REQUEST_CODE = 999999;

    //Need to keep track of the last request code delivered to avoid collisions
    private static int sLastRequestCode = 0;


    /**
     * Generates a unique request code.
     *
     * @return the request code.
     */
    private static int generateRequestCode(){
        if (sLastRequestCode >= MAX_REQUEST_CODE){
            sLastRequestCode = 0;
        }
        return ++sLastRequestCode;
    }

    /**
     * Starts the parsing process.
     *
     * @param src the string to parse.
     * @param type the type of the target object.
     * @param callback the callback object.
     * @return a request code.
     */
    public static <T extends ResultSet> int parse(@NonNull String src,
                                                  @Nullable Class<T> type,
                                                  @NonNull ParserCallback callback){

        int requestCode = generateRequestCode();
        new ParserWorker<>(requestCode, src, type, callback).execute();
        return requestCode;
    }

    /**
     * Modified default constructor. This class should not be instantiable.
     */
    private Parser(){

    }


    /**
     * Callback interface for the parser.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    public interface ParserCallback{
        /**
         * Called to potentially process the parsing result in the background if needed.
         *
         * @param requestCode the request code.
         * @param result the result set.
         */
        void onProcessResult(int requestCode, ResultSet result);

        /**
         * Called in the foreground when parsing is complete.
         *
         * @param requestCode the request code.
         * @param result the result set.
         */
        void onParseSuccess(int requestCode, ResultSet result);

        /**
         * Called in the foreground when parsing fails
         *
         * @param requestCode the request code.
         */
        void onParseFailed(int requestCode);
    }
}
