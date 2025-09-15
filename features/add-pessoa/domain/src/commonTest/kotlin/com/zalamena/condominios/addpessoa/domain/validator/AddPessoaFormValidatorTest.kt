package com.zalamena.condominios.addpessoa.domain.validator

import com.zalamena.condominios.addpessoa.domain.models.AddPessoaForm
import com.zalamena.condominios.addpessoa.domain.models.AddPessoaFormError
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddPessoaFormValidatorTest {
    private val addPessoaFormValidator: AddPessoaFormValidator by lazy { AddPessoaFormValidatorImpl() }

    @Test
    fun `GIVEN a name is filled WHEN validating THEN it should return no errors`() = runTest {
        val validName= "validName"


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(nome = validName))


        assertFalse(result.contains(AddPessoaFormError.Nome))
    }


    @Test
    fun `GIVEN a name is not filled WHEN validating THEN it should return an error with the name`() = runTest {
        val emptyName= ""


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(nome = emptyName))


        assertTrue(result.contains(AddPessoaFormError.Nome))
    }

    @Test
    fun `GIVEN a email is filled with the email regex matching WHEN validating THEN it should return no errors`() = runTest {
        val validEmail= "validEmail@validEmail.com"


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(email = validEmail))


        assertFalse(result.contains(AddPessoaFormError.Email))
    }

    @Test
    fun `GIVEN a email is not filled WHEN validating THEN it should return no errors`() = runTest {
        val emptyEmail= ""


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(email = emptyEmail))


        assertFalse(result.contains(AddPessoaFormError.Email))
    }

    @Test
    fun `GIVEN a email does not match the email regex WHEN validating THEN it should return email error`() = runTest {
        val invalidEmail= "invalidEmail"


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(email = invalidEmail))


        assertTrue(result.contains(AddPessoaFormError.Email))
    }

    @Test
    fun `GIVEN a cpf is filled with 11 characters WHEN validating THEN it should return no errors`() = runTest {
        val validCpf= "12312312312"


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(cpf = validCpf))


        assertFalse(result.contains(AddPessoaFormError.Cpf))
    }

    @Test
    fun `GIVEN a cpf is not filled WHEN validating THEN it should return no errors`() = runTest {
        val emptyCpf= ""


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(cpf = emptyCpf))


        assertFalse(result.contains(AddPessoaFormError.Cpf))
    }

    @Test
    fun `GIVEN a cpf does not match the min number of characters WHEN validating THEN it should return cpf error`() = runTest {
        val invalidCpf= "1"


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(cpf = invalidCpf))


        assertTrue(result.contains(AddPessoaFormError.Cpf))
    }

    @Test
    fun `GIVEN a phone is filled with at least 9 digits WHEN validating THEN it should return no errors`() = runTest {
        val validPhone= "123123123"


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(telefone = validPhone))


        assertFalse(result.contains(AddPessoaFormError.Telefone))
    }

    @Test
    fun `GIVEN a phone is not filled WHEN validating THEN it should return no errors`() = runTest {
        val emptyPhone= ""


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(telefone = emptyPhone))


        assertFalse(result.contains(AddPessoaFormError.Telefone))
    }

    @Test
    fun `GIVEN a phone does not have at least 9 digits WHEN validating THEN it should return phone error`() = runTest {
        val invalidPhone= "1"


        val result = addPessoaFormValidator.validate(AddPessoaForm.dummy.copy(telefone = invalidPhone))


        assertTrue(result.contains(AddPessoaFormError.Telefone))
    }
}