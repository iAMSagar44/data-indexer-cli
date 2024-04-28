export AZURE_AI_SEARCH_ENDPOINT=https://$(terraform -chdir=./infra/terraform output -raw azurerm_search_service_name).search.windows.net
export AZURE_AI_SEARCH_API_KEY=$(terraform -chdir=./infra/terraform output -raw azurerm_search_service_key)

echo AZURE_AI_SEARCH_ENDPOINT=$AZURE_AI_SEARCH_ENDPOINT
echo AZURE_AI_SEARCH_API_KEY=$AZURE_AI_SEARCH_API_KEY