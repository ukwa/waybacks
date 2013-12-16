<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

	<head>
		<title>Web Archive Session Ids</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="../stylesheets/base.css" media="all"/>
  
    </head>
    <body>
    	<div id="cstp-content" style="padding-left: 10px; padding-right: 0px; padding-top: 0px; padding-bottom: 0px;">
    	
    		<table cellpadding="0" cellspacing="0" border="0" style="width: 80%;" class="cstp-tabular-header">
            <tr>            
            	<td class="table_caption">
                	<!-- Table title -->
                	<h1>Session Id List</h1>
            	</td>
            </tr>
                        
        </table>
           	<div id="contentPanel">               							
               	<div id="pageContent">             
    				<table class="form" cellpadding="0" cellspacing="0" width="80%">                                     	
                		<c:forEach items="${sessionDetails}" var="session">
						<table class="form" style="width: 80%;">
                			<tr class="row">
   								<th style="width: 60%;" align="left">Session</th>
								<th style="width: 10%;" align="right"></th>
   								<th style="width: 3%;" align="right"></th>
							</tr>
						
        					
        						<tr class="row" style="color:blue">
        							<td class="field"><c:out value="${session.sessionId}"/></td>
									<td class="field" align="right"><c:out value="${session.sessionDetail[0].ipAddress}"/></td>
									<td class="field" align="right"><c:out value="${session.sessionDetail[0].hostName}"/></td>
        							<td class="field" align="right">
        								<a href="/archive/pages/sessionList?sessionId=<c:out value="${session.sessionId}"/>">
                							<img id="pic" alt="Delete Session" src="../images/icons/ico_deleteitem.gif" border="0"/>
                						</a>
        							
        							</td>
        						</tr>
		
        						<c:forEach items="${session.sessionDetail}" var="detail">
									<tr>                                    	
                						<td class="field"><c:out value="${detail.page}"/></td>
                					</tr>
                				</c:forEach>
								
        					</c:forEach>
        				</table>		
        			</table>
        		</div>
        	</div>
        </div>
    </body>
</html>

