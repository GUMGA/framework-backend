<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="panel panel-default">
  	<div class="panel-body">
		<form name="entityForm" method="POST" ng-submit="ctrl.save($event, entity)" gumga-form-errors gumga-ng-model-errors>
			
			<jsp:doBody />
			
			<div class="text-right">
				<input type="submit" value="Salvar" class="btn btn-primary" ng-disabled="ctrl.saving || entityForm.$invalid	" />
				<a href="#" class="btn btn-default">Cancelar</a>
			</div>
		</form>
	</div>
</div>
