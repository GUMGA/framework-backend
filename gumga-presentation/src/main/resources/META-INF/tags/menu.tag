<%@ attribute name="items" required="true" type="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://gumga.com.br/jsp/tags" prefix="g" %>
<ul>
	<c:forEach var="item" items="${items}">
		<c:choose>
  			<c:when test="${item.grupo}">
  				<li class="submenu">
  					<a tabindex="-1" <c:if test="${not empty item.id}">gumga-menu-id="${item.id}"</c:if> >${item.label}</a>
  					<c:if test="${fn:length(item.itens) > 0}">
						<g:menu items="${item.itens}" />
					</c:if>	
  				</li>
  			</c:when>			
  			<c:otherwise>
  				<li <c:if test="${not empty item.clazz}">class="${item.clazz}"</c:if> >
  					<a href="<c:url value='${item.destino}' />" <c:if test="${not empty item.id}">gumga-menu-id="${item.id}"</c:if> >${item.label}</a>
  				</li>
  			</c:otherwise>
  		</c:choose>
	</c:forEach>
</ul>