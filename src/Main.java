import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        byte[] head = new byte[2];
        byte[] data = new byte[10];
        head[0] = 'y'; head[1] = 'y';
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) ('A' + i);
        }
        File file = new File("a.txt");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(head);
        fos.write(data);
        fos.close();

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(1);
        raf.write('z');
        raf.seek(0);
        raf.write('x');
        raf.close();
    }

    public static class ZipCompressor {
        File file;
        ZipOutputStream zos;
        byte[] buff = new byte[1024];

        public ZipCompressor(File file, OutputStream os) {
            zos = new ZipOutputStream(os);
            this.file = file;
        }

        public void zip() throws IOException {
            zip(file, file.getName());
            zos.close();
        }

        private void zip(File file, String base) throws IOException {
            if (file.isDirectory()) {
                File[] fl = file.listFiles();
                if (base.length() != 0) {
                    zos.putNextEntry(new ZipEntry(base + "/"));
                }
                for (File f : fl) {
                    zip(f, base + "/" + f.getName());
                }
            } else if (file.isFile()) {
                zos.putNextEntry(new ZipEntry(base));
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int length;
                while ((length = bis.read(buff)) != -1) {
                    zos.write(buff, 0, length);
                }
                try {
                    bis.close();
                } catch (IOException ignored) {
                }
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static class ZipDecompressor {
        File file;
        ZipOutputStream zos;
        byte[] buff = new byte[1024];

        public ZipDecompressor(File file, OutputStream os) {
            zos = new ZipOutputStream(os);
            this.file = file;
        }

        public void zip() throws IOException {
            zip(file, file.getName());
            zos.close();
        }

        private void zip(File file, String base) throws IOException {
            if (file.isDirectory()) {
                File[] fl = file.listFiles();
                if (base.length() != 0) {
                    zos.putNextEntry(new ZipEntry(base + "/"));
                }
                for (File f : fl) {
                    zip(f, base + "/" + f.getName());
                }
            } else if (file.isFile()) {
                zos.putNextEntry(new ZipEntry(base));
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int length;
                while ((length = bis.read(buff)) != -1) {
                    zos.write(buff, 0, length);
                }
                try {
                    bis.close();
                } catch (IOException ignored) {
                }
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
