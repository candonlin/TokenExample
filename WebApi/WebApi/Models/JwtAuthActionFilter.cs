using Jose;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;
using System.Web.Http.Controllers;
using System.Web.Http.Filters;

namespace WebApi_JWT.Models
{
    public class JwtAuthActionFilter : ActionFilterAttribute
    {
        public override void OnActionExecuting(HttpActionContext actionContext)
        {
            // TODO: key應該移至config
            string secretKey = ConfigurationManager.AppSettings["SecretKey"];

            if (actionContext.Request.Headers.Authorization == null || actionContext.Request.Headers.Authorization.Scheme != "Bearer")
            {
                setErrorResponse(actionContext, "驗證錯誤");
            }
            else
            {
                try
                {
                    TokenClass jwtObject = JWT.Decode<TokenClass>(
                        actionContext.Request.Headers.Authorization.Parameter,
                        Encoding.UTF8.GetBytes(secretKey),
                        JwsAlgorithm.HS256);
                    if(jwtObject.Today != DateTime.Today.ToString("yyyyMMdd"))
                        setErrorResponse(actionContext, "登入憑證逾時");
                }
                catch (Exception ex)
                {
                    setErrorResponse(actionContext, ex.Message);
                }
            }

            base.OnActionExecuting(actionContext);
        }

        private static void setErrorResponse(HttpActionContext actionContext, string message)
        {
            var response = actionContext.Request.CreateErrorResponse(HttpStatusCode.Unauthorized, message);
            actionContext.Response = response;
        }
    }
}