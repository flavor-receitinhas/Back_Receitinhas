package com.flavor.recipes

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PostConstruct

@SpringBootApplication
class RecipesApplication{
	@PostConstruct
	fun init() {
		try {
			// Inicialização do FirebaseApp
			val options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.getApplicationDefault())
				.build()
			FirebaseApp.initializeApp(options)
		} catch(e: Exception){
			print(e)
		}
	}
}

fun main(args: Array<String>) {
	runApplication<RecipesApplication>(*args)
}
