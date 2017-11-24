package plugin.safe.cloud.seal;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.util.CommonUtil;
public class ForgetPasswordActivity extends BaseActivity {

    private boolean successsful;// 手机是否验证成功
    private TextView mTitleBar;
    private EditText verifyNum;
    private TextView getVerifyNum;
    private HoursePresenter mPresenter;
    private String mSendVerifyCodeAction = "http://tempuri.org/SendIdentifyCodeMsg";
    private String mCheckVerifyCodeAction = "http://tempuri.org/ValidateIdentifyCode";
    private String mForgotPasswordAction = "http://tempuri.org/ForgotPassword";
    private String mUserName;


    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_forget_password);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        mTitleBar = (TextView) findViewById(R.id.id_titlebar);
        mTitleBar.setText("忘记密码");
        mUserName = getIntent().getStringExtra("user");
        initView();
        initEvent();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what < 1) {
                getVerifyNum.setTextColor(Color.parseColor("#337ffd"));
                getVerifyNum.setText("获取验证码");
                getVerifyNum.setClickable(true);

            } else if (msg.what >= 1 && msg.what <= 60) {
                getVerifyNum.setTextColor(Color.parseColor("#cccccc"));
                getVerifyNum.setText(msg.what + "秒重新发送");
                mHandler.sendEmptyMessageDelayed(msg.what - 1, 1000);
                getVerifyNum.setClickable(false);
            } else if (msg.what == 100) {

                if (msg.obj != null) {
                    JSONObject json;
                    try {
                        json = new JSONObject((String) msg.obj);
                        String ret = json.optString("ret");
                        if (ret != null) {
                            if (ret.equals("0")) {
                                psd2nd = newPassword2nd.getText().toString();
                                forgotPassword(mUserName, psd2nd);
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "验证码输入有误！", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.what == 200) {
                String value = (String) msg.obj;
                if (msg.obj != null) {
                    JSONObject json;
                    try {
                        json = new JSONObject((String) msg.obj);
                        String ret = json.optString("ret");
                        if (ret != null) {
                            if (ret.equals("0")) {
                                Toast.makeText(ForgetPasswordActivity.this, "密码重置成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetPasswordActivity.this, LoginUserActivity.class);
                                intent.putExtra("user_name", mUserName);
                                intent.putExtra("user_password", psd2nd);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }



        }

        ;
    };
    private Button confirm;
    private EditText newPassword;
    private EditText newPassword2nd;
    private String psd2nd;
    private TextView mPhone;
    private String num;

    @Override
    public void onStatusSuccess(String action, String templateInfo) {
        super.onStatusSuccess(action, templateInfo);
        Log.e("mingguo", action + "-------" + templateInfo);
        if (action != null && templateInfo != null) {
            if (action.equals(mSendVerifyCodeAction)) {

            } else if (action.equals(mCheckVerifyCodeAction)) {
                Message message = mHandler.obtainMessage();
                message.what = 100;
                message.obj = templateInfo;
                mHandler.sendMessage(message);
            } else if (action.equals(mForgotPasswordAction)) {
                Message message = mHandler.obtainMessage();
                message.what = 200;
                message.obj = templateInfo;
                mHandler.sendMessage(message);
            }
        }
    }

    /**
     * 忘记密码
     *
     * @param phone        登录名
     * @param newPasssword 新密码
     */
    private void forgotPassword(String phone, String newPasssword) {
        String url = CommonUtil.mUserHost + "signetservice.asmx?op=ForgotPassword";
        SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mForgotPasswordAction));
        rpc.addProperty("LoginName", phone);
        rpc.addProperty("newPassword", newPasssword);
        mPresenter.readyPresentServiceParams(this, url, mForgotPasswordAction, rpc);
        mPresenter.startPresentServiceTask(true);
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void sendPhoneVerifyCode(String phone) {
        String url = "http://www.guardts.com/COMMONSERVICE/COMMONSERVICES.ASMX?op=SendIdentifyCodeMsg";
        SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mSendVerifyCodeAction));
        rpc.addProperty("phone", phone);
        mPresenter.readyPresentServiceParams(this, url, mSendVerifyCodeAction, rpc);
        mPresenter.startPresentServiceTask(true);
    }

    /**
     * 验证
     *
     * @param phone
     * @param code
     */
    private void checkPhoneVerifyCode(String phone, String code) {
        String url = "http://www.guardts.com/COMMONSERVICE/COMMONSERVICES.ASMX?op=ValidateIdentifyCode";
        SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mCheckVerifyCodeAction));
        rpc.addProperty("phone", phone);
        rpc.addProperty("number", code);
        mPresenter.readyPresentServiceParams(this, url, mCheckVerifyCodeAction, rpc);
        mPresenter.startPresentServiceTask(true);
    }

    private void initEvent() {
        // 发送验证码
        getVerifyNum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(mUserName)) {
                    sendPhoneVerifyCode(mUserName);
                    mHandler.sendEmptyMessageDelayed(60, 1000);

                }


            }
        });

        // 确认
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (checkContent()) {

                    checkPhoneVerifyCode(mUserName, num);
                }
            }
        });

    }

    private boolean checkContent() {

        if (TextUtils.isEmpty(mUserName)) {
            Toast.makeText(getApplication(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        String psd = newPassword.getText().toString();
        if (TextUtils.isEmpty(psd)) {
            Toast.makeText(getApplication(), getString(R.string.new_pwd_not_null),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (psd.length() < 6) {
            Toast.makeText(getApplication(), "密码小于6位，请重新输入",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        psd2nd = newPassword2nd.getText().toString();
        if (TextUtils.isEmpty(psd2nd)) {
            Toast.makeText(getApplication(), getString(R.string.new_again_pwd_not_null),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (psd2nd.length() < 6) {
            Toast.makeText(getApplication(), "确认密码小于6位，请重新输入",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!psd.equals(psd2nd)) {
            Toast.makeText(getApplication(), getString(R.string.twice_pwd_not_same),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        num = verifyNum.getText().toString();
        if (TextUtils.isEmpty(num)) {
            Toast.makeText(getApplication(), "验证码不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (num.length() < 6) {
            Toast.makeText(getApplication(), "验证码输入有误",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void initView() {
        mPresenter = new HoursePresenter(getApplicationContext(), this);
        mPhone = (TextView) findViewById(R.id.id_phone);
        verifyNum = (EditText) findViewById(R.id.verify_num);
        getVerifyNum = (TextView) findViewById(R.id.get_verify_num);
        confirm = (Button) findViewById(R.id.btn_confirm);
        newPassword = (EditText) findViewById(R.id.new_password);
        newPassword2nd = (EditText) findViewById(R.id.new_password_second);
        mPhone.setText(mUserName);
    }
}
