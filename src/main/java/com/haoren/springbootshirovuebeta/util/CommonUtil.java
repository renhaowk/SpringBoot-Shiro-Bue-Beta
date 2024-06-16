package com.haoren.springbootshirovuebeta.util;

import com.alibaba.fastjson.JSONObject;
import com.haoren.springbootshirovuebeta.config.exception.CommonJsonException;
import com.haoren.springbootshirovuebeta.util.constants.Constants;
import com.haoren.springbootshirovuebeta.util.constants.ErrorEnum;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

public class CommonUtil {
    public static JSONObject request2Json(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] strArray =  request.getParameterValues(paramName);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strArray.length; i++) {
                if (i == 0) {
                    sb.append(",");
                }
                sb.append(strArray[i]);
            }
            jsonObject.put(paramName, sb.toString());
        }
        return jsonObject;
    }

    /**
     * 在分页查询之前,为查询条件里加上分页参数
     *
     * @param jsonObject    查询条件json
     *                      pageNum:第几页,从1开始
     *                      pageRow:每页条数
     * @param defaultPageRow 默认的每页条数,即前端不传pageRow参数时的每页条数
     */
    public static void fillPagesParams(JSONObject jsonObject, int defaultPageRow) {
        int pageNum = jsonObject.getIntValue("pageNum");
        pageNum = pageNum == 0 ? 1 : pageNum;
        int pageRow = jsonObject.getIntValue("pageRow");
        pageRow = pageRow == 0 ? defaultPageRow : pageRow;
        jsonObject.put("offSet", (pageNum - 1) * pageRow);
        jsonObject.put("pageRow", pageRow);
        jsonObject.put("pageNum", pageNum);
    }

    public static void fillPagesParams(JSONObject jsonObject) {
        fillPagesParams(jsonObject, 10);
    }

    /**
     * 查询分页结果后的封装工具方法
     *
     * @param requestJson 请求参数json,此json在之前调用fillPageParam 方法时,已经将pageRow放入
     * @param list        查询分页对象list
     * @param totalCount  查询出记录的总条数
     */
    public static JSONObject successPage(final JSONObject requestJson, List<JSONObject> list, int totalCount) {
        int pageRow = requestJson.getIntValue("pageRow");
        int totalPage = getPageCounts(pageRow, totalCount);
        JSONObject result = successJson();
        JSONObject info = new JSONObject();
        info.put("list", list);
        info.put("totalCount", totalCount);
        info.put("totalPage", totalPage);
        result.put("info", info);
        return result;
    }

    /**
     * 查询分页结果后的封装工具方法
     *
     * @param list 查询分页对象list
     */
    public static JSONObject successPage(List<JSONObject> list) {
        JSONObject result = successJson();
        JSONObject info = new JSONObject();
        info.put("list", list);
        result.put("info", info);
        return result;
    }

    /**
     * 获取总页数
     *
     * @param pageRow   每页行数
     * @param itemCount 结果的总条数
     */
    private static int getPageCounts(int pageRow, int itemCount) {
        if (itemCount == 0) {
            return 1;
        }
        return itemCount % pageRow > 0 ?
                itemCount / pageRow + 1 :
                itemCount / pageRow;
    }

    /**
     * 返回一个info为空对象的成功消息的json
     */
    public static JSONObject successJson() {
        return successJson(new JSONObject());
    }

    /**
     * 返回一个返回码为100的json
     */
    public static JSONObject successJson(Object info) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", Constants.SUCCESS_CODE);
        resultJson.put("msg", Constants.SUCCESS_MSG);
        resultJson.put("info", info);
        return resultJson;
    }

    /**
     * 返回错误信息JSON
     */
    public static JSONObject errorJson(ErrorEnum errorEnum) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", errorEnum.getErrorCode());
        resultJson.put("msg", errorEnum.getErrorMsg());
        resultJson.put("info", new JSONObject());
        return resultJson;
    }

    public static void hasAllRequiredContent(JSONObject article, String requiredCols) {
        String[] requiredColsArr = requiredCols.split(",");
        for (String requiredCol : requiredColsArr) {
            String col = requiredCol.trim();
            if (!article.containsKey(col) || StringUtils.isEmpty(article.getString(col))) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", ErrorEnum.E_90003.getErrorCode());
                jsonObject.put("msg", ErrorEnum.E_90003.getErrorMsg());
                jsonObject.put("info", new JSONObject());
                throw new CommonJsonException(jsonObject);
            }
        }
    }
}
