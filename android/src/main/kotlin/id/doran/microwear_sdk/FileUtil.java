package id.doran.microwear_sdk;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.njj.njjsdk.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * 头像文件缓存和读取
 * Created by KB on 2017/4/14.
 */

public class FileUtil {
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
    private static final String TAG = File.class.getSimpleName();

    /**
     * 获取缓存路径，应用自己使用的数据，比如，路劲截图
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.isExternalStorageRemovable()) {
            try {
                cachePath = context.getExternalCacheDir().getPath();
                //cachePath = context.getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cachePath;
    }

    /**
     * 获取内部存储卡的缓存路径
     * @param mContext
     * @return
     */
    private static String getInnerCachePath(Context mContext) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.isExternalStorageRemovable()) {
            try {
                cachePath = mContext.getCacheDir().getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cachePath;
    }

    /**
     * 通过这个方法用于获取供外部应用调用的路径，比如拍摄头像，下载安装APK
     * @param context
     * @return
     */
    public static String getFileProviderCachePath(Context context){
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.isExternalStorageRemovable()) {
            try {
                cachePath = context.getExternalCacheDir().getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
            if (!cachePath.isEmpty()) {
                File testFile = new File(cachePath, "me_head.jpg");
                try {
                    FileProvider.getUriForFile(context, "com.mcwear.njyun.fileprovider", testFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    cachePath = getInnerCachePath(context);
                }
            }
        }
        return cachePath;
    }

    /**
     * 删除指定文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file
     */
    public static void recursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }

            for (File f : childFile) {
                recursionDeleteFile(f);
            }
            file.delete();
        }
    }

    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //避免出现内存溢出的情况，进行相应的属性设置。
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getSmallBitmap(Bitmap bm, int targetWidth, int targetHeight) {
        int srcWidth = bm.getWidth();
        int srcHeight = bm.getHeight();
        float widthScale = targetWidth * 1.0f / srcWidth;
        float heightScale = targetHeight * 1.0f / srcHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale, 0, 0);
        // 如需要可自行设置 Bitmap.Config.RGB_8888 等等
        Bitmap bmpRet = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmpRet);
        Paint paint = new Paint();
        canvas.drawBitmap(bm, matrix, paint);
        return bmpRet;
    }


    /**
     * 图片压缩方法，为了上传到服务器，保证图片大小在100K以下
     *
     * @param context
     * @param fileSrc
     * @return
     */
    public static File getSmallBitmap(Context context, String fileSrc) {
        BufferedInputStream in = null;
        BitmapFactory.Options options = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(fileSrc)));
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            options.inSampleSize = calculateInSampleSize(options, 320, 320);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(fileSrc, options);
            String fileName = context.getFilesDir() + File.separator + "head-" + bitmap.hashCode() + ".jpg";
            saveBitmap2File(bitmap, fileName);
            return new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取所有像素并保存
    public static boolean saveBitmap(Bitmap bitmap,String filePath){
        boolean bSave = false;
        byte pixels[] = new byte[bitmap.getWidth() * bitmap.getHeight() * 4];
        bitmap.copyPixelsToBuffer(ByteBuffer.wrap(pixels));
        File bmpfile = new File(filePath);
        if (bmpfile.exists()) {
            bmpfile.delete();
            try {
                bmpfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bmpfile);
            fos.write(pixels);
            fos.close();
            bSave = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bmpfile = null;

        return bSave;
    }

    public static boolean saveBitmap2(Bitmap bitmap,String src){
        int width,height;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        int mBitmapSize =width*height*2;
        boolean bSave = false;
        File bmpfile=new File(src);
        try{
            if (bmpfile.exists()) {
                bmpfile.delete();
                try {
                    bmpfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            OutputStream fout=new FileOutputStream(bmpfile);
            //   bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            //   fout.flush();
            //   fout.close();
            ByteBuffer mBuf = ByteBuffer.allocate(mBitmapSize);
            Bitmap FuBitmap=bitmap.copy(Bitmap.Config.RGB_565, false);
            FuBitmap.copyPixelsToBuffer(mBuf);
            fout.write(mBuf.array());
            fout.close();
            bSave = true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        bmpfile = null;
        return bSave;
    }

    /**
     * 设置压缩图片大小的设置参数
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRadio = Math.round(height) / reqHeight;
            int widthRadio = Math.round(width) / reqWidth;
            inSampleSize = heightRadio < widthRadio ? heightRadio : widthRadio;
        }
        return inSampleSize;
    }

    public static boolean saveBitmap2File(Bitmap bitmap, String fileName) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 50;//100表示不压缩，50表示压缩50%
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap.compress(format, quality, stream);
    }


    /**
     * 将URI转为File路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 获得路径
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

// DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            String uriStr = uri.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                Log.e(TAG, "It's auto backup pic path:" + uri.toString());
                return null;
            }
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 获取文件指定文件的指定单位的大小
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath,int sizeType){
        File file=new File(filePath);
        long blockSize=0;
        try {
            if(file.isDirectory()){
                blockSize = getFileSizes(file);
            }else{
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }
    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath){
        File file=new File(filePath);
        long blockSize=0;
        try {
            if(file.isDirectory()){
                blockSize = getFileSizes(file);
            }else{
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize);
    }
    /**
     * 获取指定文件大小
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception
    {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        else{
            file.createNewFile();
            Log.e("获取文件大小","文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++){
            if (flist[i].isDirectory()){
                size = size + getFileSizes(flist[i]);
            }
            else{
                size =size + getFileSize(flist[i]);
            }
        }
        return size;
    }
    /**
     * 转换文件大小
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS){
        Locale.setDefault(Locale.US);
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    /**
     * 转换文件大小,指定转换的类型
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double FormetFileSize(long fileS,int sizeType){
        Locale.setDefault(Locale.US);
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong=Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong=Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong=Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong=Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
    /**
     * savefile
     * 删除下载文件
     */
    public static boolean delFile(String savefile) throws IOException {
        boolean bExistsDel = false;
        File downloadFile = new File(savefile);
        if (downloadFile.exists()) {
            bExistsDel = downloadFile.delete();
        }
        downloadFile = null;
        return bExistsDel;
    }
    public static boolean saveBin2Image(byte[] byteArray, String targetPath) {
        boolean bResult = false;
        InputStream in = new ByteArrayInputStream(byteArray);
        File file = new File(targetPath);
        String path = targetPath.substring(0, targetPath.lastIndexOf("/"));
        if (!file.exists()) {
            new File(path).mkdir();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            bResult = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bResult;
    }
    public static void printHexString(byte[] b){
        System.out.println("start--->");
        for (int i = 0; i < b.length; i++)
        {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() + " ");
        }
        System.out.println("end--->");
    }
    public static void copyBigDataToSD(AssetManager assetManager, String strOutFileName,String assertFile){
        File fData = new File(strOutFileName);
        try{
            /*if (fData.exists()){
                fData.delete();
            }*/
            //if (!fData.exists()){ //not exist, copy
                InputStream myInput;
                OutputStream myOutput = new FileOutputStream(strOutFileName);
                myInput = assetManager.open(assertFile);
                byte[] buffer = new byte[1024];
                int length = myInput.read(buffer);
                while(length > 0)
                {
                    myOutput.write(buffer, 0, length);
                    length = myInput.read(buffer);
                }
                myOutput.flush();
                myInput.close();
                myOutput.close();
            //}
        }catch (Exception e ){
            e.printStackTrace();
            LogUtil.e("copyBigDataToSD ERROR!"+ e.getMessage());
        }
    }
    public static String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }

    }


    /**
     * android7.0以上处理方法
     */
    private static String getFilePathForN(Context context, Uri uri) {
        try {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = (returnCursor.getString(nameIndex));
            File file = new File(context.getFilesDir(), name);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            returnCursor.close();
            inputStream.close();
            outputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 创建文件路径
     *
     * @param context  上下文
     * @param dirNames 文件夹名
     * @return 路径
     */
    public static String createFilePath(Context context, String... dirNames) {
        if (context == null || dirNames == null || dirNames.length == 0) return null;
        File file = context.getExternalFilesDir(null);
        if (file == null || !file.exists()) return null;
        StringBuilder filePath = new StringBuilder(file.getPath());
        if (filePath.toString().endsWith("/")) {
            filePath = new StringBuilder(filePath.substring(0, filePath.lastIndexOf("/")));
        }
        for (String dirName : dirNames) {
            filePath.append("/").append(dirName);
            file = new File(filePath.toString());
            if (!file.exists() || file.isFile()) {//文件不存在
                if (!file.mkdir()) {

                    break;
                }
            }
        }
        return filePath.toString();
    }

    public static String createCacheFilePath(Context context, String... dirNames) {
        if (context == null || dirNames == null || dirNames.length == 0) return null;
        File file = context.getExternalCacheDir();
        if (file == null || !file.exists()) return null;
        StringBuilder filePath = new StringBuilder(file.getPath());
        if (filePath.toString().endsWith("/")) {
            filePath = new StringBuilder(filePath.substring(0, filePath.lastIndexOf("/")));
        }
        for (String dirName : dirNames) {
            filePath.append("/").append(dirName);
            file = new File(filePath.toString());
            if (!file.exists() || file.isFile()) {//文件不存在
                if (!file.mkdir()) {

                    break;
                }
            }
        }
        return filePath.toString();
    }

}





























