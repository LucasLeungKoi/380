<!DOCTYPE html>
<html>
<head>
  <title>Customer Support</title>
</head>
<body>
<c:url var="logoutUrl" value="/logout"/>
<form action="${logoutUrl}" method="post">
  <input type="submit" value="Log out" />
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
<c:forEach items="${tickets}" var="ticket" varStatus="status">
  <c:if test="${status.first}">
    <h2>User Name: <c:out value="${customerName}"/></h2>
    <c:forEach items="${ticketUsers}" var="ticketUser" varStatus="status">
      <c:if test="${customerName==ticketUser.username}">
        <h2>User Desc: <c:out value="${ticketUser.desc}"/></h2>
      </c:if>
    </c:forEach>
  </c:if>
  <c:if test="${customerName==ticket.customerName}">
    <c:forEach items="${ticket.attachments}" var="attachment" varStatus="status">
      <img src="<c:url value="/ticket/${ticket.id}/attachment/${attachment.id}"/>" alt="cat?" style="width:200px;height:200px;">
    </c:forEach>
  </c:if>
</c:forEach>
<br/><br/>
<a href="<c:url value="/ticket" />">Return to list tickets</a>
</body>
</html>
