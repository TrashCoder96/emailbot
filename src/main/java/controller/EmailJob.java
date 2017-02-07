package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.mail.*;
import javax.mail.search.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by asus-pc on 04.02.2017.
 */
public class EmailJob implements Job {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String updateToken(String client_id, String secret) throws IOException {
        String url = "https://login.microsoftonline.com/common/oauth2/v2.0/token";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String content = "grant_type=client_credentials&client_id=" + client_id + "&client_secret=" + secret + "&scope=https%3A%2F%2Fgraph.microsoft.com%2F.default";

        post.setEntity(new ByteArrayEntity(content.getBytes("UTF-8")));
        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        ObjectMapper mapper = new ObjectMapper();
        ResRoot resRoot = mapper.readValue(result.toString(), ResRoot.class);
        return resRoot.getAccess_token();
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imaps.port", "993");
            properties.put("mail.imaps.starttls.enable", "true");
            Session emailSession = Session.getInstance(properties);
            // emailSession.setDebug(true);

            // create the IMAP3 store object and connect with the pop server
            Store store = emailSession.getStore("imaps");

            //change the user and password accordingly

            store.connect(
                    context.getJobDetail().getJobDataMap().getString("emailserver"),
                    context.getJobDetail().getJobDataMap().getString("email"),
                    context.getJobDetail().getJobDataMap().getString("emailpassword"));
            IMAPStore imapStore = (IMAPStore) store;
            IMAPFolder in = (IMAPFolder)imapStore.getFolder("INBOX");

            in.open(Folder.READ_WRITE);

            List<javax.mail.Message> messages = Arrays.asList(in.search(new SearchTerm() {

                @Override
                public boolean match(javax.mail.Message msg) {
                    try {
                        SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LT, DateTime.now().toDate());
                        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, DateTime.now().minusHours(24).toDate());
                        SearchTerm andTerm = new AndTerm(olderThan, newerThan);
                        SearchTerm andTerm1 = new AndTerm(andTerm, new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                        return msg.match(andTerm1);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

            }));

            List<Letter> letters = new ArrayList<Letter>();
            for (javax.mail.Message m: messages) {
                Letter letter = new Letter();
                letter.setMessage(m.getSubject());
                letters.add(letter);
                Flags flags = new Flags(Flags.Flag.SEEN);
                m.setFlags(flags, true);
            }
            //Iterate through the messages
            imapStore.close();


            if (letters.size() > 0) {
                for (int i = 0; i < letters.size(); i++) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Root root = new Root();
                    root.setMessage(new controller.Message());
                    root.getMessage().setContent(letters.get(i).getMessage());
                    String json = new ObjectMapper().writeValueAsString(root);
                    okhttp3.RequestBody body = okhttp3.RequestBody.create(JSON, json);

                    okhttp3.Request request = new  okhttp3.Request.Builder()
                            .addHeader("Authorization", "Bearer " + updateToken(context.getJobDetail().getJobDataMap().getString("microsoftid"), context.getJobDetail().getJobDataMap().getString("microsoftsecret")))
                            .url("https://apis.skype.com/v2/conversations/" + context.getJobDetail().getJobDataMap().getString("skypeid") + "/activities")
                            .post(body)
                            .build();
                    okhttp3.Response response = okHttpClient.newCall(request).execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
