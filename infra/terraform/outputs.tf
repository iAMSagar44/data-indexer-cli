output "azure_storage_account" {
  value       = azurerm_storage_account.application.name
  description = "value of the storage account name"
}

output "storage_container_name" {
  value       = azurerm_storage_container.application.name
  description = "Name of the storage container"
}

output "resource_group_name" {
  value       = azurerm_resource_group.resource_group.name
  description = "Name of the resource group"
}
