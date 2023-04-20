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

<h2>Photos</h2>

<security:authorize access="hasRole('ADMIN')">
  <a href="<c:url value="/user" />">Manage User Accounts</a><br /><br />
</security:authorize>
<a href="<c:url value="/user/create" />">Create account</a><br />
<a href="<c:url value="/ticket/create" />">Upload new photo</a><br/><br/>
<c:choose>
  <c:when test="${fn:length(ticketDatabase) == 0}">
    <i>There are no tickets in the system.</i>
  </c:when>
  <c:otherwise>
    <c:forEach items="${ticketDatabase}" var="entry">
      <c:forEach items="${entry.attachments}" var="attachment" varStatus="status">
        <a href="<c:url value="/ticket/view/${entry.id}" />">
          <img src="<c:url value="/ticket/${entry.id}/attachment/${attachment.id}"/>" alt="${entry.subject}" style="width:200px;height:200px;">
        </a>
      </c:forEach>

    </c:forEach>
  </c:otherwise>
</c:choose>
</body>
</html>
