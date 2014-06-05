
public abstract class EntityMapperTranslator<BusinessObject, ServiceObject> {

	public boolean canTranslate(Class<?> targetType, Class<?> sourceType) {
		return (targetType == businessClass() && sourceType == serviceClass()) ||
				(targetType == serviceClass() && sourceType == businessClass());
	}

	protected abstract Class<BusinessObject> businessClass();

	protected abstract Class<ServiceObject> serviceClass();
	
	protected abstract BusinessObject serviceToBusiness(ServiceObject serviceObject);
	
	protected abstract ServiceObject businessToService(BusinessObject serviceObject);

	public Object translate(Object obj) {
		return null;
	}
	
}
