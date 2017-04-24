package tree.view;

import external_logic.ReadExcel;
import external_logic.ReadXMLFile;
import okhttp3.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import static tree.view.Form.buildTree;
import static tree.view.Form.createPanel;
import static tree.view.Form.tree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Random;

public class ProgressBarDemo2 extends JPanel
        implements ActionListener,
        PropertyChangeListener {

    private JProgressBar progressBar;
    private JButton startButton;
    private JButton OKButton;
    private JTextArea taskOutput;
    private Task task;
    private static JFrame frame;
    public JPanel pnl;
    public Form f;
    public static File inputResource;
    public String output;

    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            /*Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            //Sleep for at least one second to simulate "startup".
            try {
                Thread.sleep(1000 + random.nextInt(2000));
            } catch (InterruptedException ignore) {}
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }*/
            setProgress(0);
            FinConverter converter = new FinConverter();
            System.out.println(converter.inputResource);
            if(converter.inputResource != null) {
                setProgress(10);
            }
            output = converter.createCustomXML();
            if(!output.isEmpty()) {
                setProgress(90);
            }

            File f1 = new File(Paths.get("").toAbsolutePath().toString() + "\\.xml");
            if(f1.exists()) {
                f1.delete();
            }

            File f2 = new File(Paths.get("").toAbsolutePath().toString() + "\\current.odt");
            if(f2.exists()) {
                f2.delete();
            }

            File f3 = new File(Paths.get("").toAbsolutePath().toString() + "\\myFormat.xls");
            if(f3.exists()) {
                f3.delete();
            }

            if(FilenameUtils.getExtension(inputResource.getAbsolutePath()).equals("doc") ||
                    FilenameUtils.getExtension(inputResource.getAbsolutePath()).equals("docx")) {
                output = Paths.get("").toAbsolutePath().toString() + "\\_convert.xml";
            } else {
                if(FilenameUtils.getExtension(inputResource.getAbsolutePath()).equals("xls")) {
                    output = Paths.get("").toAbsolutePath().toString() + "\\_convert.xml";
                }
            }
            setProgress(100);
            return null;
        }

        /*
         * Executed in event dispatch thread
         */
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            taskOutput.append("Документ готов к обработке!\n");
        }
    }

    public ProgressBarDemo2(JPanel pnl, Form f, File inputResource) {
        super(new BorderLayout());

        this.pnl = pnl;
        this.f = f;
        this.inputResource = inputResource;

        //Create the demo's UI.
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);

        OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buildTree(output, 0, null);
                pnl.add(new JScrollPane(tree), BorderLayout.CENTER);
                pnl.add(createPanel(), BorderLayout.SOUTH);
                f.revalidate();
                frame.setVisible(false);
                frame.dispose();
            }
        });
        OKButton.setEnabled(false);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);

        //Call setStringPainted now so that the progress bar height
        //stays the same whether or not the string is shown.
        progressBar.setStringPainted(true);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(OKButton);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
        progressBar.setIndeterminate(true);
        startButton.setEnabled(false);
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName() || progressBar.getValue() == 20 ||
                progressBar.getValue() == 30 || progressBar.getValue() == 40 || progressBar.getValue() == 50
                || progressBar.getValue() == 75 || progressBar.getValue() == 80 || progressBar.getValue() == 85
                || progressBar.getValue() == 89) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setIndeterminate(false);
            progressBar.setValue(progress);
            switch(progress) {
                case 10:
                    taskOutput.append("Файл успешно загружен. Процесс подготовки окружения...\n");
                    break;
                case 20:
                    taskOutput.append("Конфигурирование параметров конвертации.\n");
                    break;
                case 30:
                    taskOutput.append("Завершение конфигурирования...\n");
                    break;
                case 40:
                    taskOutput.append("Конфигурирование успешно завершено\n");
                    break;
                case 50:
                    taskOutput.append("Загрузка преобразованного файла\n");
                    break;
                case 75:
                    taskOutput.append("Файл успешно получен\n");
                    break;
                case 80:
                    taskOutput.append("Подготовка пользовательского окружения\n");
                    break;
                case 85:
                    taskOutput.append("Завершение настройки окружения...\n");
                    break;
                case 89:
                    taskOutput.append("Окружение успешно инстанцировано\n");
                    break;
                case 90:
                    taskOutput.append("Создан файл текущего сеанса в формате xml.\n");
                    break;
                case 100:
                    startButton.setVisible(false);
                    OKButton.setEnabled(true);
                    taskOutput.append(String.format(
                            "Готово! Нажмите \"ОК\" и начните работу с документом!\n", progress));
                    break;
            }
        }
    }

    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    public static void createAndShowGUI(JPanel pnl, Form f, File inputResource) {
        //Create and set up the window.
        frame = new JFrame("ProgressBarDemo2");
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        //Create and set up the content pane.
        JComponent newContentPane = new ProgressBarDemo2(pnl, f, inputResource);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public class FinConverter {

        String online_converter_ID = "-1";
        File inputResource = ProgressBarDemo2.inputResource;
        MediaType mediaType;
        OkHttpClient client;
        String job_ID;
        StringBuilder outputResource = new StringBuilder(Paths.get("").toAbsolutePath().toString());


        private String createCustomXML() {


            client = new OkHttpClient();

            switch(FilenameUtils.getExtension(inputResource.getAbsolutePath())) {
                case "doc":
                    mediaType =
                            MediaType.parse("application/msword");
                    convertToODT("odt", outputResource.append("\\current.odt"));
                    new ReadXMLFile().convertFile(outputResource.toString());
                    break;
                case "docx":
                    mediaType =
                            MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    convertToODT("odt", outputResource.append("\\current.odt"));
                    new ReadXMLFile().convertFile(outputResource.toString());
                    break;
                case "xls":
                    new ReadExcel().convertExcelToXml(inputResource.getAbsolutePath());
                    break;
                case "xlsx":
                /*mediaType =
                        MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");*/
                    //convertToODT("xls", outputResource.append("\\current.xls"));
                    //new ReadExcel().convertExcelToXml(inputResource);
                    //new ReadExcel().convertExcelToXml("V:\\6.xls");
                    break;
            }
            return outputResource.toString();
        }

        private void convertToODT (String type, StringBuilder outputResource) {

            RequestBody body = null;

            body = RequestBody.create(mediaType, "{\"conversion\":[{\"target\":\"odt\"}]}");

            File file = new File(inputResource.getAbsolutePath());

            RequestBody requestForUploadBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file))
                    .build();
            try {
//21cd5cfb47566d6ec9d2467979c57881
//995f166033ea1548653db41051ad1670

                online_converter_ID = "995f166033ea1548653db41051ad1670";
                Request initilizeJOB = new Request.Builder()
                        .url("http://api2.online-convert.com/jobs")
                        .post(body)
                        .addHeader("x-oc-api-key", online_converter_ID)
                        .addHeader("cache-control", "no-cache")
                        .build();
                Response res = client.newCall(initilizeJOB).execute();

                if (res.code() == 401) {
                    online_converter_ID = "c0889234cb2ad47231961a6ba1bda4f8";
                    initilizeJOB = new Request.Builder()
                            .url("http://api2.online-convert.com/jobs")
                            .post(body)
                            .addHeader("x-oc-api-key", online_converter_ID)
                            .addHeader("cache-control", "no-cache")
                            .build();
                    client.newCall(initilizeJOB).execute();
                }

                progressBar.setValue(20);

                Request getAllJOBs = new Request.Builder()
                        .url("http://api2.online-convert.com/jobs")
                        .addHeader("x-oc-api-key", online_converter_ID)
                        .addHeader("cache-control", "no-cache")
                        .build();

                Response allJOBs = client.newCall(getAllJOBs).execute();

                progressBar.setValue(30);

                String curr =allJOBs.body().string().replace("\\", "").replaceAll("\"", "");

                job_ID = StringUtils.substringBetween(curr, "id:", ",token");

                System.out.println("JOB: " + job_ID);

                progressBar.setValue(40);

                String server = StringUtils.substringBetween(curr, "server:", ",spent");
                System.out.println("Server: " + server);


                Request uploadFile = new Request.Builder()
                        .url(server + "/upload-file/" + job_ID)
                        .post(requestForUploadBody)
                        .addHeader("x-oc-api-key", online_converter_ID)
                        .build();

                progressBar.setValue(50);

                client.newCall(uploadFile).execute();

                progressBar.setValue(75);

                allJOBs = client.newCall(getAllJOBs).execute();
                curr = StringUtils.substringBetween(allJOBs.body().string().replace("\\", "").replaceAll("\"", ""), "code:", ",info");
                System.out.println(curr);

                progressBar.setValue(80);

                while(curr.equals("processing")) {
                    allJOBs = client.newCall(getAllJOBs).execute();
                    curr = StringUtils.substringBetween(allJOBs.body().string().replace("\\", "").replaceAll("\"", ""), "code:", ",info");
                }

                progressBar.setValue(85);

                if(curr.equals("completed")) {

                    allJOBs = client.newCall(getAllJOBs).execute();

                    URL website = new URL(StringUtils.substringBetween(allJOBs.body().string().replace("\\", "").replaceAll("\"", ""), "uri:", ",size"));
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(outputResource.toString());
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    rbc.close();

                    Request request3 = new Request.Builder()
                            .url("https://api2.online-convert.com/jobs/" + job_ID)
                            .delete()
                            .addHeader("x-oc-api-key", online_converter_ID)
                            .addHeader("cache-control", "private, must-revalidate")
                            .build();
                    client.newCall(request3).execute();

                }
                progressBar.setValue(89);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}

