package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Recommender {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> recommend(Book targetBook, int numberOfRecommendations) {
        List<Book> allBooks = bookRepository.findAll();

        System.out.println(allBooks);

        Map<Book, Double> bookScores = new HashMap<>();

        for (Book book : allBooks) {
            System.out.println(book + "book");
            if (!book.equals(targetBook)) {
                double score = computeSimilarity(targetBook, book);
                System.out.println("score" + score);
                bookScores.put(book, score);
            }
        }

        System.out.println(bookScores + "Diem tuong dong");

        return bookScores.entrySet().stream()
                .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
                .limit(numberOfRecommendations)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double computeSimilarity(Book book1, Book book2) {
        System.out.println("computeSimilarity");
    
        Map<String, Double> tfIdf1 = computeTfIdf(book1);
        Map<String, Double> tfIdf2 = computeTfIdf(book2);
    
        if (tfIdf1 == null || tfIdf2 == null) {
            
            return 0.0;
        }
    
        double score = cosineSimilarity(tfIdf1, tfIdf2);
        
        return score;
    }
    

    private Map<String, Double> computeTfIdf(Book book) {
        if (book == null) {
           
            return null;
        }
    
        System.out.println("vao duoc computeTfIdf");
        Map<String, Double> tfIdf = new HashMap<>();
    
        if (book.getAuthor() != null) {
            System.out.println("Processing author: " + book.getAuthor());
            for (String token : book.getAuthor().split("\\s+")) {
                tfIdf.put("author_" + token, tfIdf.getOrDefault("author_" + token, 0.0) + 1);
            }
        } else {
            System.out.println("Author is null");
        }
    
        if (book.getGenre() != null) {
            System.out.println("Processing genre: " + book.getGenre());
            for (String token : book.getGenre().split("\\s+")) {
                tfIdf.put("genre_" + token, tfIdf.getOrDefault("genre_" + token, 0.0) + 1);
            }
        } else {
            System.out.println("Genre is null");
        }
    
        if (book.getDescription() != null) {
            System.out.println("Processing description: " + book.getDescription());
            for (String token : book.getDescription().split("\\s+")) {
                tfIdf.put("description_" + token, tfIdf.getOrDefault("description_" + token, 0.0) + 1);
            }
        } else {
            System.out.println("Description is null");
        }
    
        System.out.println("TF map trước khi chuẩn hóa: " + tfIdf);
    
        if (tfIdf.isEmpty()) {
            System.out.println("TF-IDF map is empty");
            return null;
        }
    
        double maxFreq = Collections.max(tfIdf.values());
        tfIdf.replaceAll((k, v) -> v / maxFreq);
    
        System.out.println("TF-IDF map sau khi chuẩn hóa: " + tfIdf);
    
        return tfIdf;
    }
    
    
    private double cosineSimilarity(Map<String, Double> tfIdf1, Map<String, Double> tfIdf2) {
        System.out.println("vao duoc cosineSimilarity");
        Set<String> allFeatures = new HashSet<>(tfIdf1.keySet());
        allFeatures.addAll(tfIdf2.keySet());
    
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
    
        for (String feature : allFeatures) {
            double val1 = tfIdf1.getOrDefault(feature, 0.0);
            double val2 = tfIdf2.getOrDefault(feature, 0.0);
            dotProduct += val1 * val2;
            normA += val1 * val1;
            normB += val2 * val2;
        }
    
        System.out.println("Dot product: " + dotProduct);
        System.out.println("NormA: " + normA);
        System.out.println("NormB: " + normB);
    
        return (normA == 0 || normB == 0) ? 0.0 : dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    
}
