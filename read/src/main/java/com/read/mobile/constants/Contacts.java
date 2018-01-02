package com.read.mobile.constants;

public class Contacts {
	/**
	 * 请求地址
	 */
	public static final String URL = "http://123.56.104.127/";

	/**
	 * 秘钥
	 */
	public static final String KEY = "fdf4da319ea90b3cdb861887c77a75ec";

	/**
	 * 书目列表 OK
	 */
	public static final String URL_BOOK_BOOK_LIST = URL + "book/bookList";
	/**
	 * 写笔记
	 */
	public static final String URL_BOOK_WRITE_NOTES = URL + "book/writeNotes";
	/**
	 * 搜索书目 OK
	 */
	public static final String URL_BOOK_SEARCH = URL + "book/search";
	/**
	 * 搜索书目 OK
	 */
	public static final String URL_BOOK_MULTYSEARCH = URL + "book/multySearch";
	/**
	 * 笔记列表 OK
	 */
	public static final String URL_BOOK_NOTE_LIST = URL + "book/noteList";
	/**
	 * 用户信息 ok
	 */
	public static final String URL_USER_INFO = URL + "user/info";
	/**
	 * 收藏列表 ok
	 */
	public static final String URL_BOOK_FAVORLIST = URL + "book/favorList";
	/**
	 * 用户登录 OK
	 */
	public static final String URL_USER_LOGIN = URL + "user/login";
	/**
	 * 用户注册 OK
	 */
	public static final String URL_USER_REGISTER = URL + "user/register";
	/**
	 * 获取验证码 OK
	 */
	public static final String URL_USER_GETCAPTCHA = URL + "user/getCaptcha";
	/**
	 * 重置用户密码 OK
	 */
	public static final String URL_USER_RESETPWD = URL + "user/resetPwd";
	/**
	 * 更新用户信息 OK
	 */
	public static final String URL_USER_UPDATEUSERINFO = URL + "user/updateUserInfo";
	/**
	 * 修改用户密码 OK
	 */
	public static final String URL_USER_CHANGEPASSWD = URL + "user/changepasswd";
	/**
	 * 推荐列表 OK
	 */
	public static final String URL_BOOK_REC_BOOKLIST = URL + "book/recList";
	/**
	 * 图书详情 OK
	 */
	public static final String URL_BOOK_INFO = URL + "book/info";
	/**
	 * 图片上传 OK
	 */
	public static final String URL_USER_UPLOADIMG = URL + "user/uploadImg";
	/**
	 * 小孩信息 OK
	 */
	public static final String URL_CHILD_INFO = URL + "child/info";
	/**
	 * 添加小孩信息 OK
	 */
	public static final String URL_CHILD_ADDCHILD = URL + "child/addChild";
	/**
	 * 获取用户已笔记书目列表 OK
	 */
	public static final String URL_BOOK_GETNOTEBOOKLIST = URL + "book/getNoteBookList";
	/**
	 * 收藏图书 OK
	 */
	public static final String URL_BOOK_STOREBOOK = URL + "book/storeBook";

	/**
	 * 成功返回码
	 */
	public static final String RESULT_CODE_OK = "20000";
	/**
	 * 是否还有下一页
	 */
	public static final String RESULT_HAS_NEXT_YES = "1";
	/**
	 * 分页 每页数目
	 */
	public static final String REQUEST_PAGE_LENGTH = "16";

	/**
	 * 关键字查询
	 */
	public static final String STYPE_KEYWORD = "keyword";
	/**
	 * isbn查询
	 */
	public static final String STYPE_ISBN = "isbn";
	/**
	 * 分类查询
	 */
	public static final String STYPE_RANGE = "range";
	/**
	 * 分类查询-儿童读物
	 */
	public static final String SEARCH_KEYSTR_ETDW = "1";
	/**
	 * 分类查询-少儿精选
	 */
	public static final String SEARCH_KEYSTR_SEJX = "2";
	/**
	 * 分类查询-青年书籍
	 */
	public static final String SEARCH_KEYSTR_QNSJ = "3";
	/**
	 * 性别-男
	 */
	public static final String SEX_NAN = "1";
	/**
	 * 性别-女
	 */
	public static final String SEX_NV = "0";
	/**
	 * 笔记类型-文字笔记
	 */
	public static final String NOTE_TYPE_CHART = "1";
	/**
	 * 笔记类型-语音笔记
	 */
	public static final String NOTE_TYPE_VOICE = "2";

	public static final String PATH_AUDIO = "audiobox";

}
