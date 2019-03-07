function ueTextFormatter(value){
	value = value.replace(/(\n)/g, "");  
 	value = value.replace(/(\t)/g, "");  
 	value = value.replace(/(\r)/g, "");  
	value = value.replace(/<\/?[^>]*>/g, "");  
	value = value.replace(/\s*/g, "");
	return value;
}