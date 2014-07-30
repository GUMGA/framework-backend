<%@ attribute name="values" required="true" %>
<%@ attribute name="showSearch" required="false" %>
<%@ attribute name="showRemove" required="false" %>
<%@ attribute name="showInsert" required="false" %>
<%@ attribute name="rowNgClass" required="false" %>
<%@ attribute name="gridColumns" required="true" fragment="true" %>
<%@ attribute name="searchFields" required="false" fragment="true" %>
<%@ attribute name="buttons" required="false" fragment="true" %>



<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="panel panel-default">
  <div class="panel-body">

		
		<div class="row">
			<div id="buttons" class="col-md-5" style="margin-bottom: 10px;">
				<jsp:invoke fragment="buttons" var="buttons"/>
				<c:if test="${not empty buttons}">${buttons}</c:if>
				<c:if test="${empty buttons}">
					<c:if test="${showInsert != 'false'}">
						<a href="#/insert" class="btn btn-primary" id="btnNovo">
							<span class="glyphicon glyphicon-plus"></span> 
							<fmt:message key="app.button.novo" />
						</a>
					</c:if>
					<c:if test="${showRemove != 'false'}">
						<button id="btnExcluir" class="btn btn-danger" ng-click="ctrl.removeSelection()" ng-disabled="selection.length == 0">
							<span class="glyphicon glyphicon-trash"></span> 
							<fmt:message key="app.button.remove" />
						</button>
					</c:if>
				</c:if>
			</div>
		
			<c:if test="${showSearch != 'false'}">
				<div class="col-md-7">
					<gumga:search on-search="ctrl.search($text, $fields)" search-text="search.text" select-fields="search.fields">
						<jsp:invoke fragment="searchFields" />
					</gumga:search>
				</div>
			</c:if>
			
			<div class="col-xs-12">
				<gumga:table 
						values="${values}"
						class="table-condensed table-striped"
						selectable="multiple"
						selection="selection"
						on-sort="ctrl.doSort($column, $direction)"
						sort-by="sort.field"
						sort-direction="sort.direction"
						empty-values-message="Sem resultados"
						<c:if test="${rowNgClass != null}">row-ng-class="${rowNgClass}"</c:if>
						>
					<jsp:invoke fragment="gridColumns" />
				</gumga:table>
				
		        <gumga:pagination ng-show="numberOfPages > 1" items-per-page="list.pageSize" total-items="list.count" ng-model="page" num-pages="numberOfPages" boundary-links="true"></gumga:pagination>
			</div>
			
		</div>

  </div>
</div>