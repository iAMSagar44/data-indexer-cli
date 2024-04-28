output "azurerm_search_service_name" {
  value = azurerm_search_service.search.name
}

output "azurerm_search_service_id" {
  value = azurerm_search_service.search.id
}

output "azurerm_search_service_key" {
  value     = azurerm_search_service.search.primary_key
  sensitive = true
}