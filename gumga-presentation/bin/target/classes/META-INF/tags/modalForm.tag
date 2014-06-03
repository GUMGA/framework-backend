<%@ attribute name="title" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<div class="modal-header">
	<h3 class="modal-title">${title}</h3>
</div>
<div class="modal-body">
	<form name="${name}" gumga-form-errors gumga-ng-model-errors>
		<jsp:doBody />
	</form>
</div>
<div class="modal-footer">
    <button class="btn btn-primary" ng-click="gumgaModalController.confirm()" ng-disabled="${name}.$invalid || !gumgaModalController.validateModal()"><fmt:message key="app.modal.confirm" /></button>
    <button class="btn btn-default" ng-click="gumgaModalController.cancel()"><fmt:message key="app.modal.cancel" /></button>
</div>