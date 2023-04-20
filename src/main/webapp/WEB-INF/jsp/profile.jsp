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
    <h2>Ticket #: <c:out value="${customerName}"/></h2>
  </c:if>
  <c:if test="${customerName==ticket.customerName}">
    <c:forEach items="${ticket.attachments}" var="attachment" varStatus="status">
      <img src="<c:url value="/ticket/${ticket.id}/attachment/${attachment.id}"/>" alt="cat?" style="width:200px;height:200px;">
    </c:forEach>
  </c:if>

</c:forEach>
<a href="<c:url value="/ticket" />">Return to list tickets</a>
</body>
</html>
