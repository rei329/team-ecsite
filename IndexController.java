package jp.co.internous.peppermill.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.internous.peppermill.model.domain.MstCategory;
import jp.co.internous.peppermill.model.domain.MstProduct;
import jp.co.internous.peppermill.model.form.SearchForm;
import jp.co.internous.peppermill.model.mapper.MstCategoryMapper;
import jp.co.internous.peppermill.model.mapper.MstProductMapper;
import jp.co.internous.peppermill.model.session.LoginSession;

/**
 * 商品検索に関する処理を行うコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/peppermill")
public class IndexController {
	
	@Autowired
	private LoginSession loginSession;
	
	@Autowired
	private MstCategoryMapper categoryMapper;
	
	@Autowired
	private MstProductMapper productMapper;
	
	/**
	 * トップページを初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return トップページ
	 */
	@RequestMapping("/")
	public String index(Model m) {
		
		/* 未ログイン状態かつ仮ユーザーIDを保持していない場合は
		 * 仮ユーザーID(負の9桁のランダムな数字)を作成し、セッションに保持する*/
		if(!loginSession.isLogined() && loginSession.getTmpUserId() == 0) {
			Random random = new Random();
			int randomNumber = -1 * random.nextInt(1000000000);
			loginSession.setTmpUserId(randomNumber);
		}

		//login_headerのloginSession属性用
		m.addAttribute("loginSession", loginSession);
		
		//indexのcategories属性用、プルダウンの選択肢を取得した内容を設定
		List<MstCategory> categories = categoryMapper.find();
		m.addAttribute("categories", categories);
		
		//indexのselected属性用、初期表示時のプルダウンは未選択、検索時は指定したカテゴリー名を選択
		m.addAttribute("selected", 0);
		
		//indexのproducts属性用
		List<MstProduct> products = productMapper.find();
		m.addAttribute("products", products);
		
		return "index";
	}
	
	/**
	 * 検索処理を行う
	 * @param f 検索用フォーム
	 * @param m 画面表示用オブジェクト
	 * @return トップページ
	 */
	@RequestMapping("/searchItem")
	public String searchItem(SearchForm f, Model m) {
		
		//login_headerのloginSession属性用
		m.addAttribute("loginSession", loginSession);

		//indexのcategories属性用、プルダウンの選択肢を取得した内容を設定
		List<MstCategory> categories = categoryMapper.find();
		m.addAttribute("categories", categories);

		//indexのselected属性用、初期表示時のプルダウンは未選択、検索時は指定したカテゴリー名を選択
		m.addAttribute("selected", f.getCategory()); 

		// 以下indexのkeywords属性用
		String keywords = f.getKeywords().replaceAll("　", " ").replaceAll("\\s{2,}", " ").trim();
		m.addAttribute("keywords", keywords);
				
		//indexのproducts属性用、商品情報の検索処理を行い、商品情報を取得
		List<MstProduct> products = null;
		if (f.getCategory() == 0) {
			products = productMapper.findByProductName(keywords.split(" "));
		} else {
			products = productMapper.findByCategoryAndProductName(f.getCategory(), keywords.split(" "));
		}
		m.addAttribute("products", products);
		
		return "index";
	}
}
