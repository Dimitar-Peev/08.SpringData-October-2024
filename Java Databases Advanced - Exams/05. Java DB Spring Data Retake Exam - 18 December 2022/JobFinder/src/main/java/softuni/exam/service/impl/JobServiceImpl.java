package softuni.exam.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.JobSeedDto;
import softuni.exam.models.dto.xml.JobSeedRootDto;
import softuni.exam.models.entity.Company;
import softuni.exam.models.entity.Job;
import softuni.exam.repository.CompanyRepository;
import softuni.exam.repository.JobRepository;
import softuni.exam.service.JobService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private static final String FILE_PATH = "src/main/resources/files/xml/jobs.xml";

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlMapper xmlMapper;
    private final CompanyRepository companyRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, ModelMapper modelMapper, ValidationUtil validationUtil,
                          XmlMapper xmlMapper, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlMapper = xmlMapper;
        this.companyRepository = companyRepository;
    }

    @Override
    public boolean areImported() {
        return this.jobRepository.count() > 0;
    }

    @Override
    public String readJobsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importJobs() throws IOException {
        StringBuilder output = new StringBuilder();

        JobSeedRootDto jobSeedRootDto = this.xmlMapper.readValue(readJobsFileContent(), JobSeedRootDto.class);

        jobSeedRootDto.getJobs()
                .stream()
                .filter(jobSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(jobSeedDto);

                    output.append(isValid ? "Successfully imported job " + jobSeedDto.getJobTitle()
                                    : "Invalid job")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newJob)
                .forEach(jobRepository::save);

        return output.toString().trim();
    }

    private Job newJob(JobSeedDto jobSeedDto) {
        Job job = this.modelMapper.map(jobSeedDto, Job.class);

        Company existingCompany = this.companyRepository.findById(jobSeedDto.getCompanyId());
        job.getCompanies().add(existingCompany);

        return job;
    }

    @Override
    public String getBestJobs() {

        List<Job> jobs = this.jobRepository
                .findJobBySalaryGreaterThanEqualAndHoursAWeekLessThanEqualOrderBySalaryDesc(5000.00, 30.00);

        StringBuilder output = new StringBuilder();
        jobs.forEach(job -> output.append(job.toString()).append(System.lineSeparator()));
        return output.toString();
    }
}
