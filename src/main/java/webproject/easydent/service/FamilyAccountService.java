package webproject.easydent.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import webproject.easydent.entities.FamilyAccount;
import webproject.easydent.entities.User;
import webproject.easydent.repositories.FamilyRepository;
import webproject.easydent.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FamilyAccountService {
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    @Transactional
    public FamilyAccount createFamilyGroup(User leader, String memberEmail, String relationship) {
        String familyGroupId = generateFamilyGroupId(memberEmail, relationship);
        Optional<FamilyAccount> existingAccount = familyRepository.findById(Long.parseLong(familyGroupId));

        if (existingAccount.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 가족 관계입니다.");
        }


        User member = userRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("초대할 구성원을 찾을 수 없습니다."));

        if (leader.getEmail().equals(memberEmail)) {
            throw new IllegalArgumentException("자신을 가족 구성원으로 초대할 수 없습니다.");
        }

        FamilyAccount familyAccount = new FamilyAccount();
        familyAccount.setId(familyGroupId);
        familyAccount.setCreatedAt(LocalDateTime.now());
        familyAccount.setRelationship(relationship);
        familyAccount.setLeader(leader);
        familyAccount.setMember(member);

        leader.setFamilyAccount(familyAccount);
        leader.setIsFamilyLeader(true);
        member.setFamilyAccount(familyAccount);
        member.setIsFamilyLeader(false);

        familyRepository.save(familyAccount);
        userRepository.save(leader);
        userRepository.save(member);

        return familyAccount;
    }

    private String generateFamilyGroupId(String email, String relationship) {
        String emailPrefix = email.split("@")[0];
        return emailPrefix + "_" + relationship;
    }

    @Transactional
    public FamilyAccount getFamilyAccount(User user) {
        // 사용자의 가족 계정 조회
        Optional<FamilyAccount> familyAccount = familyRepository.findByLeaderOrMember(user, user);

        if (familyAccount.isPresent()) {
            return familyAccount.get();
        }
        return null;
    }
 }
