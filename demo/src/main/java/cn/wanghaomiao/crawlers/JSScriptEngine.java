package cn.wanghaomiao.crawlers;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 直接调用js代码
 */
public class JSScriptEngine {
    public static String decode(String pripid) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {

            engine.eval("function Base64() {\n" +
                    " // private property\n" +
                    " _keyStr = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=\";\n" +
                    " // public method for encoding\n" +
                    " this.encode = function (input) {\n" +
                    "  var output = \"\";\n" +
                    "  var chr1, chr2, chr3, enc1, enc2, enc3, enc4;\n" +
                    "  var i = 0;\n" +
                    "  input = _utf8_encode(input);\n" +
                    "  while (i < input.length) {\n" +
                    "   chr1 = input.charCodeAt(i++);\n" +
                    "   chr2 = input.charCodeAt(i++);\n" +
                    "   chr3 = input.charCodeAt(i++);\n" +
                    "   enc1 = chr1 >> 2;\n" +
                    "   enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);\n" +
                    "   enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);\n" +
                    "   enc4 = chr3 & 63;\n" +
                    "   if (isNaN(chr2)) {\n" +
                    "    enc3 = enc4 = 64;\n" +
                    "   } else if (isNaN(chr3)) {\n" +
                    "    enc4 = 64;\n" +
                    "   }\n" +
                    "   output = output +\n" +
                    "   _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +\n" +
                    "   _keyStr.charAt(enc3) + _keyStr.charAt(enc4);\n" +
                    "  }\n" +
                    "  \n" +
                    "  var rep=replaceAll(output,\"+\",\"u002B\");\n" +
                    "  rep=replaceAll(rep,\"=\",\"u002C\");\n" +
                    "  rep=replaceAll(rep,\"/\",\"u002D\");\n" +
                    "  return rep;\n" +
                    " }\n" +
                    " // public method for decoding\n" +
                    " this.decode = function (input) {\n" +
                    "\tinput=replaceAll(input,\"u002B\",\"+\");\n" +
                    "\tinput=replaceAll(input,\"u002C\",\"=\");\n" +
                    "\tinput=replaceAll(input,\"u002D\",\"/\");\n" +
                    "  var output = \"\";\n" +
                    "  var chr1, chr2, chr3;\n" +
                    "  var enc1, enc2, enc3, enc4;\n" +
                    "  var i = 0;\n" +
                    "  input = input.replace(/[^A-Za-z0-9\\+\\/\\=]/g, \"\");\n" +
                    "  while (i < input.length) {\n" +
                    "   enc1 = _keyStr.indexOf(input.charAt(i++));\n" +
                    "   enc2 = _keyStr.indexOf(input.charAt(i++));\n" +
                    "   enc3 = _keyStr.indexOf(input.charAt(i++));\n" +
                    "   enc4 = _keyStr.indexOf(input.charAt(i++));\n" +
                    "   chr1 = (enc1 << 2) | (enc2 >> 4);\n" +
                    "   chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);\n" +
                    "   chr3 = ((enc3 & 3) << 6) | enc4;\n" +
                    "   output = output + String.fromCharCode(chr1);\n" +
                    "   if (enc3 != 64) {\n" +
                    "    output = output + String.fromCharCode(chr2);\n" +
                    "   }\n" +
                    "   if (enc4 != 64) {\n" +
                    "    output = output + String.fromCharCode(chr3);\n" +
                    "   }\n" +
                    "  }\n" +
                    "  output = _utf8_decode(output);\n" +
                    "  return output;\n" +
                    " }\n" +
                    " // private method for UTF-8 encoding\n" +
                    " _utf8_encode = function (string) {\n" +
                    "  string = string.replace(/\\r\\n/g,\"\\n\");\n" +
                    "  var utftext = \"\";\n" +
                    "  for (var n = 0; n < string.length; n++) {\n" +
                    "   var c = string.charCodeAt(n);\n" +
                    "   if (c < 128) {\n" +
                    "    utftext += String.fromCharCode(c);\n" +
                    "   } else if((c > 127) && (c < 2048)) {\n" +
                    "    utftext += String.fromCharCode((c >> 6) | 192);\n" +
                    "    utftext += String.fromCharCode((c & 63) | 128);\n" +
                    "   } else {\n" +
                    "    utftext += String.fromCharCode((c >> 12) | 224);\n" +
                    "    utftext += String.fromCharCode(((c >> 6) & 63) | 128);\n" +
                    "    utftext += String.fromCharCode((c & 63) | 128);\n" +
                    "   }\n" +
                    "  }\n" +
                    "  return utftext;\n" +
                    " }\n" +
                    " // private method for UTF-8 decoding\n" +
                    " _utf8_decode = function (utftext) {\n" +
                    "  var string = \"\";\n" +
                    "  var i = 0;\n" +
                    "  var c = c1 = c2 = 0;\n" +
                    "  while ( i < utftext.length ) {\n" +
                    "   c = utftext.charCodeAt(i);\n" +
                    "   if (c < 128) {\n" +
                    "    string += String.fromCharCode(c);\n" +
                    "    i++;\n" +
                    "   } else if((c > 191) && (c < 224)) {\n" +
                    "    c2 = utftext.charCodeAt(i+1);\n" +
                    "    string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));\n" +
                    "    i += 2;\n" +
                    "   } else {\n" +
                    "    c2 = utftext.charCodeAt(i+1);\n" +
                    "    c3 = utftext.charCodeAt(i+2);\n" +
                    "    string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));\n" +
                    "    i += 3;\n" +
                    "   }\n" +
                    "  }\n" +
                    " \n" +
                    "  return string;\n" +
                    " }\n" +
                    "}\n" +
                    "\n" +
                    "function replaceAll(str, sptr, sptr1){\n" +
                    "\n" +
                    "\twhile (str.indexOf(sptr) >= 0)\n" +
                    "\n" +
                    "\t{\n" +
                    "\n" +
                    "\t  str = str.replace(sptr, sptr1);\n" +
                    "\n" +
                    "\t}\n" +
                    "\n" +
                    "\treturn str;\n" +
                    "\n" +
                    "}");

            engine.eval("var b = new Base64(); var tt = b.encode('" + pripid +"');");
            Object tt = engine.get("tt");
            return tt.toString();
            // engine.eval("alert(\"js alert\");");    // 不能调用浏览器中定义的js函数 // 错误，会抛出alert引用不存在的异常
        } catch (ScriptException e) {

            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String decode = decode("360681210037532");
        System.out.println("decode = " + decode);
    }
}