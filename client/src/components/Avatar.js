/* eslint-disable react/prop-types */
import styled from 'styled-components';

const SAvatar = styled.div`
  img {
    border-radius: 5px;
  }
`;

const Avatar = ({ size, image }) => {
  return (
    <SAvatar>
      <img
        src={image || null} //기본이미지 설정
        width={size}
        height={size}
        alt="profile"
        // style={{ objectFit: 'cover' }}
      />
    </SAvatar>
  );
};

export default Avatar;
