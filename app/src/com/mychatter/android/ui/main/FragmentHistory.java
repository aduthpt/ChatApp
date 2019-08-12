package com.mychatter.android.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mychatter.android.R;
import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.accounts.UserAccountManager;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.auth.HttpAccess;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.util.JSONObjectHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FragmentHistory extends Fragment {


    ListView list;
    RecyclerView recyclerView;

    public FragmentHistory() {
    }

    //truyền code vào đây để hiển thị danh sách history chat
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        list = (ListView) view.findViewById(R.id.listcontact);
        ArrayList<String> stringList = new ArrayList<>();


        AsyncTask<String, Void, RestResponse> result = new AsyncExample().execute();

        RestResponse restResponse = null;
        UserAccountManager userAccMgr = SalesforceSDKManager.getInstance().getUserAccountManager();
        List<UserAccount> users = userAccMgr.getAuthenticatedUsers();

        String path = users.get(0).getInstanceServer();
        int presentNameLen = users.get(0).getDisplayName().length();
        try {

            restResponse = result.get();
            JSONObject jsonObject = restResponse.asJSONObject();
            JSONObjectHelper jsonObjectHelper = new JSONObjectHelper();
            String[] str = jsonObjectHelper.optStringArray(jsonObject, "conversations");
            System.out.println("VIP: "+Arrays.toString(str));
            String name = "";
            //System.out.println("conversation size: "+jsonObjectHelper.optStringArray(jsonObject, "recipients").length);
            for (int m = 0; m < jsonObjectHelper.optStringArray(jsonObject, "conversations").length; m++) {
                for (int i = 0; i < str[m].length(); i++) {

                    if (str[m].substring(i, i + 11).equals("displayName")) {
                        for (int j = i + 14; j < str[m].length(); j++) {
                            if (str[m].charAt(j) == '"') break;
                            else name += str[m].charAt(j);

                        }
                        if(!name.equals(users.get(0).getDisplayName())){
                            stringList.add(name.substring(presentNameLen));
                            name = "";
                            break;
                        }

                    }
                }
            }

            //System.out.println("VIP2: "+name);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        CustomAdapter adapter = new CustomAdapter(stringList, getActivity());
        list.setAdapter(adapter);


        return view;
    }

    //    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        String[] items = getResources().getStringArray(R.array.tab_text_1);
//        RecycleViewAdapter adapter = new RecycleViewAdapter(items);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//    }
    private class AsyncExample extends AsyncTask<String, Void, RestResponse> {
        @Override
        protected RestResponse doInBackground(String... params) {
            UserAccountManager userAccMgr = SalesforceSDKManager.getInstance().getUserAccountManager();
            List<UserAccount> users = userAccMgr.getAuthenticatedUsers();

            String path = users.get(0).getInstanceServer();
            Map<String, String> headers = new HashMap<String, String>();
            //headers.put("X-Foo", "x-foo-header");
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            //headers.put("method", "GET");
            headers.put("Authorization", "Bearer " + users.get(0).getAuthToken());
            RestRequest restRequest = new RestRequest(RestRequest.RestMethod.GET, path + "/services/data/v45.0/chatter/users/me/conversations", headers);

            System.out.println(users.get(0).getAuthToken());
            HttpAccess httpAccess = new HttpAccess(null, "dummy-agent");
            RestClient.ClientInfo clientInfo = null;
            try {
                clientInfo = new RestClient.ClientInfo(
                        new URI(users.get(0).getInstanceServer()),
                        new URI(users.get(0).getLoginServer()),
                        new URI(users.get(0).getIdUrl()),
                        users.get(0).getAccountName(),
                        users.get(0).getUsername(),
                        users.get(0).getUserId(),
                        users.get(0).getOrgId(),
                        users.get(0).getCommunityId(),
                        users.get(0).getCommunityUrl(),
                        users.get(0).getFirstName(),
                        users.get(0).getLastName(),
                        users.get(0).getDisplayName(),
                        users.get(0).getEmail(),
                        users.get(0).getPhotoUrl(),
                        users.get(0).getThumbnailUrl(),
                        users.get(0).getAdditionalOauthValues()

                );
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            RestResponse response = null;
            RestClient restClient = new RestClient(clientInfo, users.get(0).getAuthToken(), HttpAccess.DEFAULT, null);
            try {
                response = restClient.sendSync(restRequest);

            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject json = null;
            try {
                json = response.asJSONObject();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(RestResponse result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
