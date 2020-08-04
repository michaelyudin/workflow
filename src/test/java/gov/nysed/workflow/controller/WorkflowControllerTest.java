package gov.nysed.workflow.controller;

import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.domain.repository.ResultRepository;
import gov.nysed.workflow.domain.repository.WorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void testWorkflowSuccessful() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/wf/replace-reg"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        result.getResponse().getContentAsString();

        UUID resultId = ((WorkflowResult)result.getModelAndView().getModel().get("result")).getId();

        this.mockMvc.perform(
                post("/wf/replace-reg")
                        .param("_step", "result")
                        .param("_result_uuid", resultId.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Attestation")));

        this.mockMvc.perform(
                post("/wf/replace-reg")
                        .param("_step", "attestation")
                        .param("_result_uuid", resultId.toString())
                        .param("signature", "Mike Yudin"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("ALL DONE")));
    }

    @Test
    @Transactional
    void testJumpForwardThrowsException() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/wf/replace-reg"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        result.getResponse().getContentAsString();

        UUID resultId = ((WorkflowResult)result.getModelAndView().getModel().get("result")).getId();

        this.mockMvc.perform(
                post("/wf/replace-reg")
                        .param("_step", "result")
                        .param("_result_uuid", resultId.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Attestation")));

        this.mockMvc.perform(
                get("/wf/replace-reg/thank-you?_result_uuid=" + resultId.toString()))
                .andDo(print()).andExpect(status().is5xxServerError());
    }


    @Test
    @Transactional
    void testJumpBackIsSuccessful() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/wf/replace-reg"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        result.getResponse().getContentAsString();

        UUID resultId = ((WorkflowResult)result.getModelAndView().getModel().get("result")).getId();

        this.mockMvc.perform(
                post("/wf/replace-reg")
                        .param("_step", "result")
                        .param("_result_uuid", resultId.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Attestation")));

        this.mockMvc.perform(
                get("/wf/replace-reg/result?_result_uuid=" + resultId.toString()))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void testBadStepUrlThrows404() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/wf/replace-reg"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        result.getResponse().getContentAsString();

        UUID resultId = ((WorkflowResult)result.getModelAndView().getModel().get("result")).getId();

        this.mockMvc.perform(
                post("/wf/replace-reg")
                        .param("_step", "result")
                        .param("_result_uuid", resultId.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Attestation")));

        this.mockMvc.perform(
                get("/wf/replace-reg/SOME-STEP-THAT-DOESNT-EXIST?_result_uuid=" + resultId.toString()))
                .andDo(print()).andExpect(status().isNotFound());
    }

}