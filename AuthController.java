package jp.co.internous.peppermill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jp.co.internous.peppermill.model.domain.MstUser;
import jp.co.internous.peppermill.model.form.UserForm;
import jp.co.internous.peppermill.model.mapper.MstUserMapper;
import jp.co.internous.peppermill.model.mapper.TblCartMapper;
import jp.co.internous.peppermill.model.session.LoginSession;


/**
 * 認証に関する処理を行うコントローラー
 * @author インターノウス
 *
 */
@RestController
@RequestMapping("/peppermill/auth")
public class AuthController {

	//フィールド定義
	
	@Autowired
	private MstUserMapper userMapper;
	
	@Autowired
	private TblCartMapper cartMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	private Gson gson = new Gson();
	
	/**
	 * ログイン処理をおこなう
	 * @param f ユーザーフォーム
	 * @return ログインしたユーザー情報(JSON形式)
	 */
	@PostMapping("/login")
	public String login(@RequestBody UserForm f) {
		
		//認証(DBの会員情報マスタテーブルにユーザー名とパスワードが一致するユーザーが存在するか確認)
		MstUser user = userMapper.findByUserNameAndPassword(f.getUserName(), f.getPassword());
		
		//仮ユーザーID
		int tmpUserId = loginSession.getTmpUserId();
		
		/* 【認証が成功した場合】
		 * 仮ユーザーIDに紐づくカート情報をユーザーIDに紐づけ直しログインセッションをログイン成功の内容に変更
		 * 【認証が失敗した場合】
		 * ログインセッションをログイン失敗の内容に変更する*/
		if(user != null) {
			cartMapper.updateUserId(user.getId(), tmpUserId);
			loginSession.setUserId(user.getId());
			loginSession.setTmpUserId(0);
			loginSession.setUserName(user.getUserName());
			loginSession.setPassword(user.getPassword());
			loginSession.setLogined(true);
		} else {
			loginSession.setUserId(0);
			loginSession.setUserName(null);
			loginSession.setPassword(null);
			loginSession.setLogined(false);
		}
		
		return gson.toJson(user);
	}
	
	/**
	 * ログアウト処理をおこなう
	 * @return 空文字
	 */
	@PostMapping("/logout")
	public String logout() {
		
		//ログインセッションをログアウト処理の内容に変更する
		loginSession.setUserId(0);
		loginSession.setTmpUserId(0);
		loginSession.setUserName(null);
		loginSession.setPassword(null);
		loginSession.setLogined(false);
		
		return "";
	}

	/**
	 * パスワード再設定をおこなう
	 * @param f ユーザーフォーム
	 * @return 処理後のメッセージ
	 */
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestBody UserForm f) {
		
		//my_pageでエラーが無い場合、DBの会員情報マスタテーブルのパスワードとセッションのパスワードを入力値で更新
		try {
			userMapper.updatePassword(f.getUserName(), f.getNewPassword());
			loginSession.setPassword(f.getNewPassword());
		} catch(Exception e) {
			System.out.println("パスワードの更新に失敗しました" + e.getMessage());
		}
		
		return "パスワードが再設定されました。";
	}
}
