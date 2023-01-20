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
        src={
          image ||
          'https://cdn.imweb.me/upload/S20211026228188315d8e6/590e88b6bb53b.jpg'
        } //기본이미지 설정
        width={size}
        height={size}
        alt="profile"
        // style={{ objectFit: 'cover' }}
      />
    </SAvatar>
  );
};

export default Avatar;
