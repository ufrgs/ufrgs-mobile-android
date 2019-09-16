package com.cpd.utils

import com.cpd.network.models.Ru

/**
 * Created by theolm on 19/07/16.
 */
object RuUtils {

    val listSemCardapio: List<Ru>
        get() {
            val ruList = arrayListOf<Ru>()

            val ru1 = Ru()
            ru1.nome = "Ru1 - Centro"
            ru1.cardapio = "Não existe cardápio para hoje."

            val ru2 = Ru()
            ru2.nome = "Ru2 - Saúde"
            ru2.cardapio = "Não existe cardápio para hoje."

            val ru3 = Ru()
            ru3.nome = "Ru3 - Vale"
            ru3.cardapio = "Não existe cardápio para hoje."

            val ru4 = Ru()
            ru4.nome = "Ru4 - Agronomia"
            ru4.cardapio = "Não existe cardápio para hoje."

            val ru5 = Ru()
            ru5.nome = "Ru5 - Esefid"
            ru5.cardapio = "Não existe cardápio para hoje."

            val ru6 = Ru()
            ru6.nome = "Ru6 - Bloco 4"
            ru6.cardapio = "Não existe cardápio para hoje."

            ruList.add(ru1)
            ruList.add(ru2)
            ruList.add(ru3)
            ruList.add(ru4)
            ruList.add(ru5)
            ruList.add(ru6)

            return ruList
        }

    val listError: List<Ru>
        get() {
            val ruList = arrayListOf<Ru>()
            val ru1 = Ru()
            ru1.nome = "Erro"
            ru1.cardapio = "Erro ao adquirir informações"
            ruList.add(ru1)
            return ruList
        }

}
