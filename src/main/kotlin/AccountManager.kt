

class AccountManager {
    private val accounts: List<Account> = listOf(
        Account("Ahmed Maher", "many@gmail.com", "123456"),
        Account("Mahmoud", "mahmoud@gmail.com", "123456"),
    )

    private fun findAccountByEmail(email: String): Account? {
        return accounts.find { it.email == email }
    }

    fun login(email: String, password: String): Account? {
        val account = findAccountByEmail(email)
        if (account != null && account.password == password) {
            return account
        }
        return null
    }
}