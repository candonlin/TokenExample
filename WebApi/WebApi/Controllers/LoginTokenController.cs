using Jose;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Cryptography;
using System.Text;
using System.Web.Http;
using WebApi_JWT.Models;

namespace WebApi_JWT.Controllers
{
    public class LoginTokenController : ApiController
    {
        public IHttpActionResult LoginCheck(UserModel user)
        {
            try
            {
                LoginResult loginrst = new LoginResult();
                string secretKey = ConfigurationManager.AppSettings["SecretKey"];   //Json Web Token加密Key
                TokenClass token = new TokenClass();
                token.UserID = user.Name;
                token.Today = DateTime.Today.ToString("yyyyMMdd");
                loginrst.result = true;
                loginrst.rtntoken = JWT.Encode(token, Encoding.UTF8.GetBytes(secretKey), JwsAlgorithm.HS256);
                return Ok(loginrst);
            }
            catch
            {
                throw new UnauthorizedAccessException("登入驗證失敗!");
            }
        }        
    }
}
