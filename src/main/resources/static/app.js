const API_URL = "http://localhost:8080/api/students";

async function createStudent(studentData) {
    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(studentData)
        });

        if (response.ok){
            alert("Student added successfully!");
            resetForm();
        } else {
            alert("Error adding student.");
        }
    } catch (error) {
        console.error("Error creating student:", error);
    }
}

document.getElementById("studentForm").addEventListener("submit", function(e){
    e.preventDefault();

    const id = document.getElementById("studentId").ariaValueMax;
    const studentData = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value
    };
    createStudent(studentData);
});

function resetForm(){
    document.getElementById("studentId").value = "";
    document.getElementById("studentForm").reset();

    const submitBtn = document.getElementById("submitBtn");
    submitBtn.textContent = "Add Student";
    submitBtn.style.backgroundColor = "#28a745";
}

function validateStudentForm(){
    let isValid = true;

    const firstName = document.getElementById("firstName");
    const lastName = document.getElementById("lastName");
    const email = document.getElementById("email");

    const firstNameError = document.getElementById("firstNameError");
    const lastNameError = document.getElementById("lastNameError");
    const emailError = document.getElementById("emailError");
}