package com.picaproject.pica.Util;

import android.net.Uri;
import android.util.Log;

import com.picaproject.pica.Item.LoginResultItem;
import com.picaproject.pica.Item.PictuerDetailItem;
import com.picaproject.pica.Item.UploadImageItem;
import com.picaproject.pica.Util.NetworkItems.AlbumResultItem;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;
import com.picaproject.pica.Util.NetworkItems.MemberRegisterItem;
import com.picaproject.pica.Util.NetworkItems.MyAlbumResultListItem;
import com.picaproject.pica.Util.NetworkItems.ProfileResultItem;
import com.picaproject.pica.Util.NetworkItems.ReplyResultItem;

import java.io.File;
import java.util.ArrayList;

import dagger.internal.GenerationOptions;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class NetworkUtility {
    private static NetworkUtility instance;
    private Retrofit retrofit;
    private ApiService apiService;
    private NetworkUtility(){}

    public static NetworkUtility getInstance() {
        if (instance == null)
            instance = new NetworkUtility();

        return instance;
    }

    private Retrofit getRetrofitClient() {
        //If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {
            //Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(UrlList.BASE_URL) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }

    public void uploadProfile(Uri fileUri, int memberID, Callback<DefaultResultItem> callback) {
        String filePath = AppUtility.getAppinstance().getRealPathFromUri(fileUri);
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                Retrofit retrofit = getRetrofitClient();
                NetworkUtility.ApiService apiService = retrofit.create(NetworkUtility.ApiService.class);
                // creates RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                // MultipartBody.Part is used to send also the actual filename
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                RequestBody ids = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(memberID));

                Call<DefaultResultItem> call = apiService.uploadProfileService(body, ids);
                call.enqueue(callback);
            }
        }
    }
    public void createNewAlbum(Uri fileUri, String name, String description, int cretor_id, Callback<DefaultResultItem> callback){
        String filePath = AppUtility.getAppinstance().getRealPathFromUri(fileUri);
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                Retrofit retrofit = getRetrofitClient();
                NetworkUtility.ApiService apiService = retrofit.create(NetworkUtility.ApiService.class);
                // creates RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                // MultipartBody.Part is used to send also the actual filename
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                RequestBody names =
                        RequestBody.create(MediaType.parse("text/plain"), name);

                RequestBody descs =
                        RequestBody.create(MediaType.parse("text/plain"), description);
                RequestBody ids = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(cretor_id));

                Call<DefaultResultItem> call = apiService.createAlbumService(body, names, descs, ids);
                call.enqueue(callback);

            }

        }

    }

    public void registerMember(MemberRegisterItem item, Callback<DefaultResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<DefaultResultItem> call = apiService.registerMemberService(item.email, item.password, item.nickname, item.phonenumber);
        call.enqueue(callback);
    }

    public void loginMember(String email, String pw, Callback<LoginResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LoginResultItem> call = apiService.loginService(email, pw);
        call.enqueue(callback);
    }

    public void getMyAlbumList(int memberId, Callback<MyAlbumResultListItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<MyAlbumResultListItem> call = apiService.getMyAlbumListService(memberId);
        call.enqueue(callback);
    }

    public void getAlbumPhotoList(int albumid, int memberid, Callback<AlbumResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<AlbumResultItem> call = apiService.getMyAlbumPhotoService(albumid, memberid);
        call.enqueue(callback);
    }

    public void getPictureReplyList(int pictureid, int memberid, Callback<ReplyResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ReplyResultItem> call = apiService.getPictureReplyService(pictureid, memberid);

        call.enqueue(callback);
    }

    public void addPictureReply(int picureid, int memberid, String reply, Callback<DefaultResultItem> callback){
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<DefaultResultItem> call = apiService.addReplyService(picureid, memberid, reply);

        call.enqueue(callback);
    }

    public void myfavoritePictureList(int memberId, Callback<AlbumResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<AlbumResultItem> call = apiService.getMyFavoritePictureListService(memberId);
        call.enqueue(callback);
    }

    public void myUploadPictureList(int memberId, Callback<AlbumResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<AlbumResultItem> call = apiService.getMyUploadPictureListService(memberId);
        call.enqueue(callback);
    }

    public void getPictureSearchList(int memberId, String word, Callback<AlbumResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<AlbumResultItem> call = apiService.getSearchListService(memberId, word);
        call.enqueue(callback);
    }

    public void deletePictureReply(int replyId, int memberID, Callback<DefaultResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<DefaultResultItem> call = apiService.deleteReplyService(memberID, replyId);

        call.enqueue(callback);
    }

    public void getProfilePhoto(int memberId, Callback<ProfileResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<ProfileResultItem> call = apiService.getProfileService(memberId);
        call.enqueue(callback);
    }


    public void doFavortePhoto(int memberId, int pictureId, Callback<DefaultResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<DefaultResultItem> call = apiService.doFavoriteService(memberId, pictureId);
        call.enqueue(callback);
    }
    public void getPictureDetail(int pictureId, int memberid, Callback<PictuerDetailItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<PictuerDetailItem> call = apiService.getPhotoDetailService(pictureId, memberid);
        call.enqueue(callback);
    }
    /**
     * 업로드 해야할것
     * file, contents, tags 합친것, latitude, longitude,
     *
     * @Part MultipartBody.Part realfile, @Part("latitude") RequestBody latitude, @Part("longitude") RequestBody longitude,
     * @Part("contents") RequestBody contents, @Part("p_member_id") RequestBody p_member_id, @Part("p_album_id") RequestBody p_album_id, @Part("tags") RequestBody tags
     */

    public void uploadSinglePictureToAlbum(int albumId, int memberId, UploadImageItem uploadImageItem, Callback<DefaultResultItem> callback){
        Log.i("upload Image Service", uploadImageItem.toString());

        Uri fileUri = Uri.parse(uploadImageItem.getSrc());
        String filePath = AppUtility.getAppinstance().getRealPathFromUri(fileUri);
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                Retrofit retrofit = getRetrofitClient();
                NetworkUtility.ApiService apiService = retrofit.create(NetworkUtility.ApiService.class);
                // creates RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                // MultipartBody.Part is used to send also the actual filename
                MultipartBody.Part body = MultipartBody.Part.createFormData("realfile", file.getName(), requestFile);
                RequestBody latitude =
                        RequestBody.create(MediaType.parse("text/plain"), Double.toString(uploadImageItem.getLocation().getLatitude()));
                RequestBody longitude =
                        RequestBody.create(MediaType.parse("text/plain"), Double.toString(uploadImageItem.getLocation().getLongitude()));

                String tmpContent = "";
                if (uploadImageItem.getContents() != null)
                    tmpContent = uploadImageItem.getContents();
                RequestBody contents =
                        RequestBody.create(MediaType.parse("text/plain"), tmpContent);
                RequestBody memberIdbody =
                        RequestBody.create(MediaType.parse("text/plain"), Integer.toString(memberId));
                RequestBody albumIdBody =
                        RequestBody.create(MediaType.parse("text/plain"), Integer.toString(albumId));
                RequestBody tags =
                        RequestBody.create(MediaType.parse("text/plain"), uploadImageItem.parsingTagString());

                Call<DefaultResultItem> call = apiService.uploadPictureService(body, latitude, longitude, contents, memberIdbody, albumIdBody, tags);
                call.enqueue(callback);

            }

        }
    }

    /*
    public void uploadPictureToAlbum(int albumId, int memberId, ArrayList<UploadImageItem> imageList, Callback<DefaultResultItem> callback) {
        Retrofit retrofit = getRetrofitClient();
        NetworkUtility.ApiService apiService = retrofit.create(NetworkUtility.ApiService.class);

        for (int i = 0; i< imageList.size(); i++) {
            String filePath = AppUtility.getAppinstance().getRealPathFromUri(imageList.get(i).getSrc());
            if (filePath != null && !filePath.isEmpty()) {
                File file = new File(filePath);
                if (file.exists()) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    // MultipartBody.Part is used to send also the actual filename
                    MultipartBody.Part body = MultipartBody.Part.createFormData("realfile", file.getName(), requestFile);

                    RequestBody names =
                            RequestBody.create(MediaType.parse("text/plain"), name);

                    RequestBody descs =
                            RequestBody.create(MediaType.parse("text/plain"), description);

                    RequestBody ids = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(cretor_id));

                    Call<DefaultResultItem> call = apiService.createAlbumService(body, names, descs, ids);
                    call.enqueue(callback);
                } else {
                    //파일이 존재하지 않는경우
                }

            }
        }
    }*/


    public interface ApiService {
        @Multipart
        @POST(UrlList.CREATE_ALBUM_URL)
        Call<DefaultResultItem> createAlbumService(@Part MultipartBody.Part file, @Part("name") RequestBody name, @Part("description") RequestBody description, @Part("create_p_member_id") RequestBody member_id);

        @FormUrlEncoded
        @POST(UrlList.REGISTER_MEMBER_URL)
        Call<DefaultResultItem> registerMemberService(@Field("email") String email, @Field("password") String pw, @Field("nickname") String nickname, @Field("phonenumber") String phonenumber);

        @FormUrlEncoded
        @POST(UrlList.LOGIN_URL)
        Call<LoginResultItem> loginService(@Field("email") String email, @Field("password") String pw);

        @Multipart
        @POST(UrlList.UPLOAD_PROFILE_URL)
        Call<DefaultResultItem> uploadProfileService(@Part MultipartBody.Part file, @Part("member_id") RequestBody id);

        @GET(UrlList.GET_PROFILE_URL)
        Call<ProfileResultItem> getProfileService(@Query("member_id") int memberId);

        @GET(UrlList.GET_SEARCH_PICTURE_URL)
        Call<AlbumResultItem> getSearchListService(@Query("member_id") int memberId, @Query("word") String word);

        @GET(UrlList.GET_MY_ALBUM_LIST_URL)
        Call<MyAlbumResultListItem> getMyAlbumListService(@Query("member_id") int memberID);

        @GET(UrlList.MY_FAVORITE_PICTURE_URL)
        Call<AlbumResultItem> getMyFavoritePictureListService(@Query("member_id") int memberID);

        @GET(UrlList.MY_UPLOAD_PICTURE_URL)
        Call<AlbumResultItem> getMyUploadPictureListService(@Query("member_id") int memberID);

        @GET(UrlList.SHOW_MY_ALBUM_URL)
        Call<AlbumResultItem> getMyAlbumPhotoService(@Query("album_id") int albumId, @Query("member_id") int memberId);


        @GET(UrlList.SHOW_REPLY_URL)
        Call<ReplyResultItem> getPictureReplyService(@Query("picture_id") int pictureId, @Query("member_id") int member_id);

        @POST(UrlList.FAVORITE_URL)
        Call<DefaultResultItem> doFavoriteService(@Query("member_id") int memberId, @Query("picture_id") int pictureId);

        @FormUrlEncoded
        @POST(UrlList.ADD_REPLY_URL)
        Call<DefaultResultItem> addReplyService(@Field("picture_id") int pictureid, @Field("member_id") int memberid, @Field("reply_text") String reply);

        @FormUrlEncoded
        @POST(UrlList.DELETE_REPLY_URL)
        Call<DefaultResultItem> deleteReplyService( @Field("member_id") int memberid, @Field("reply_id") int replyId);

        @FormUrlEncoded
        @GET(UrlList.SHOW_PICTURE_DETAIL_URL)
        Call<PictuerDetailItem> getPhotoDetailService(@Field("picture_id") int pictureId, @Field("member_id") int memberId);

        @Multipart
        @POST(UrlList.UPLOAD_PICTURE_URL)
        Call<DefaultResultItem> uploadPictureService(@Part MultipartBody.Part realfile,
                                                     @Part("latitude") RequestBody latitude, @Part("longitude") RequestBody longitude,
                                                     @Part("contents") RequestBody contents, @Part("p_member_id") RequestBody p_member_id, @Part("p_album_id") RequestBody p_album_id, @Part("tags") RequestBody tags);
    }

    public class APIRESULT {
        //공통으로 사용
        public static final int RESULT_SUCCESS = 0;
        public static final int RESULT_FAIL = -1;

        public static final int RESULT_WRONG_PW = -2;
        public static final int RESULT_NO_EMAIL = -3;
        //회원가입
        public static final int RESULT_REGISTER_ALREADY_EXIST = 1;

        //앨범생성
        public static final int RESULT_CREATEALBUM_SERVER_ERROR = -1;
        public static final int RESULT_CREATEALBUM_DB_ERROR = -2;

        //내 앨범보기 (멤버없음), 앨범조회 (앨범없음), 사진조회 (사진없음)
        public static final int RESULT_NOT_EXIST = -1;
        public static final int RESULT_MYALBUM_FILE_ERROR = -2;

        //앨범조회 (가져오기실패, 권한없음), 사진상세보기(가져오기실패, 권한없음)
        public static final int RESULT_FILE_GET_ERROR = -2;
        public static final int RESULT_NO_AUTHOR = -3;
    }
}
