terraform {
  required_version = ">=1.0"
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~>3.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~>3.0"
    }
  }
}
provider "azurerm" {
  skip_provider_registration = true
  features {}
}

resource "azurerm_search_service" "search" {
  name                = var.azurerm_search_service_name
  resource_group_name = var.RG
  location            = var.resource_group_location
  sku                 = var.sku
  replica_count       = var.replica_count
  partition_count     = var.partition_count
}