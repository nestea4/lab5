package edu.panchoshna.lab5;/*
    @author User
    @project lab5
    @class ArchitectureTests
    @version 1.0.0
    @since 18.04.2025 - 00.25 
*/

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static org.junit.platform.commons.util.Preconditions.condition;

@SpringBootTest
public class ArchitectureTests {
    private JavaClasses applicationClasses;

    @BeforeEach
    void initialize() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("edu.panchoshna.lab5");
    }

    @Test
    void shouldFollowLayerArchitecture() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                .layer("Model").definedBy("..model..")
                //
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                //
                .check(applicationClasses);
    }

    @Test
    void controllersShouldNotDependOnOtherControllers() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .check(applicationClasses);
    }

    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..service..")
                .check(applicationClasses);
    }

    @Test
    void controllerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);
    }

    @Test
    void controllerClassesShouldBeAnnotatedByRestController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .check(applicationClasses);
    }

    @Test
    void repositoryShouldBeInterface() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .beInterfaces()
                .check(applicationClasses);
    }

    @Test
    void anyControllerFieldsShouldNotBeAnnotatedAutowired() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should().beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }

    @Test
    void modelFieldsShouldBePrivate() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..model..")
                .should().bePrivate()
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldBeNamedXService() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .haveSimpleNameEndingWith("Service")
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldBeAnnotatedByService() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .beAnnotatedWith(org.springframework.stereotype.Service.class)
                .check(applicationClasses);
    }

    @Test
    void repositoryClassesShouldBeNamedXRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .haveSimpleNameEndingWith("Repository")
                .check(applicationClasses);
    }

    @Test
    void repositoryClassesShouldBeAnnotatedByRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .beAnnotatedWith(org.springframework.stereotype.Repository.class)
                .check(applicationClasses);
    }

    @Test
    void serviceCrudMethodsShouldBePublic() {
        methods()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..service..")
                .and().haveNameNotMatching("init")
                .should().bePublic()
                .check(applicationClasses);
    }

    @Test
    void modelsShouldNotDependOnOtherLayers() {
        noClasses()
                .that().resideInAPackage("..model..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("..controller..", "..service..", "..repository..")
                .check(applicationClasses);
    }

    @Test
    void controllersShouldNotUseRepositoriesDirectly() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..repository..")
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldBeAnnotatedWithDocument() {
        classes()
                .that().resideInAPackage("..model..")
                .should()
                .beAnnotatedWith(org.springframework.data.mongodb.core.mapping.Document.class)
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldHaveIdField() {
        fields()
                .that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .and().haveName("id")
                .should().beAnnotatedWith(org.springframework.data.annotation.Id.class)
                .check(applicationClasses);
    }

    @Test
    void controllerMethodsShouldHaveHttpAnnotations() {
        methods()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..controller..")
                .should().beAnnotatedWith(org.springframework.web.bind.annotation.GetMapping.class)
                .orShould().beAnnotatedWith(org.springframework.web.bind.annotation.PostMapping.class)
                .orShould().beAnnotatedWith(org.springframework.web.bind.annotation.PutMapping.class)
                .orShould().beAnnotatedWith(org.springframework.web.bind.annotation.DeleteMapping.class)
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldHaveEqualsMethod() {
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .and().haveName("equals")
                .and().haveRawParameterTypes(Object.class)
                .should().bePublic()
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldHaveHashCodeMethod() {
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .and().haveName("hashCode")
                .should()
                .bePublic()
                .andShould().haveRawReturnType(int.class)
                .check(applicationClasses);
    }
}
