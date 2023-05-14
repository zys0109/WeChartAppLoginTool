# WeChartAppLoginTool

# 1. 微信登录-第一步

## 1.1 接口说明

接口url：/GPTAuthLogin/user/getSessionId

请求方式: get

请求参数:

| 名称 | 类型   | 描述                       |
| ---- | ------ | -------------------------- |
| code | String | 临时登录凭证code，微信传递 |

返回数据:

~~~json
https://localhost:8080/GPTAuthLogin/user/getSessionId

{
  "code": 0,
  "message": "操作成功！",
  "data": {
  	"sessionId":"ssss"
  }
}
~~~



# 2. 微信登录-第二步

## 2.1 接口说明

接口url：/GPTAuthLogin/user/authLogin

请求方式: post

请求参数:

| 名称          | 类型   | 描述                         |
| ------------- | ------ | ---------------------------- |
| encryptedData | String | 微信传递的加密数据，后端解密 |
| iv            | string | 微信传递，解密算法初始向量   |
| sessionId     | string | 第一步传递前端的sessionId    |

返回数据:

~~~json
https://localhost:8080/GPTAuthLogin/user/authLogin

{"code":0,"data":{"background":"","gender":"女","id":6,"nickname":"微信用户","phoneNumber":"122","portrait":"https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NiwiZXhwIjoxNjI5MzA0NDYyfQ.MZnlOARueap1r_yhoujeMrUzNRs7xvuPBuT9JG9Tpf0"},"message":"操作成功！"}
~~~



# 3. 微信登录-第三步-获取用户信息并重置登录状态



## 3.1 接口说明

接口url：/GPTAuthLogin/user/userinfo

请求方式: get

请求参数:

| 名称    | 类型 | 描述                 |
| ------- | ---- | -------------------- |
| refresh | bool | true刷新 false不刷新 |

返回数据:

~~~json
https://localhost:8080/GPTAuthLogin/user/userinfo

{"code":0,"data":{"background":"","gender":"女","id":6,"nickname":"微信用户","phoneNumber":"122","portrait":"https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NiwiZXhwIjoxNjI5MzA0NDYyfQ.MZnlOARueap1r_yhoujeMrUzNRs7xvuPBuT9JG9Tpf0"},"message":"操作成功！"}
~~~

